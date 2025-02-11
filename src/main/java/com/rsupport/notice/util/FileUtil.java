package com.rsupport.notice.util;

import com.rsupport.notice.domain.entity.Attachment;
import com.rsupport.notice.properties.FileStorageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUtil {

    private static final String path = System.getProperty("user.dir");
    private final FileStorageProperties properties;

    /*
     * 파일 업로드
     * */
    public List<Attachment> uploadFile(List<MultipartFile> fileList) {
        return Optional.ofNullable(fileList)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(file -> !file.isEmpty())
                .map(this::storeFile)
                .collect(Collectors.toList());
    }

    /*
     * 파일 저장
     * */
    private Attachment storeFile(MultipartFile file) {
        try {
            String uniqueFileName = uniqueFileName(file.getOriginalFilename());

            Path uploadPath = Paths.get(path + properties.getUploadDir()).normalize();
            Files.createDirectories(uploadPath);
            Path targetLocation = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            log.info("파일 저장 완료: {}", targetLocation);

            return Attachment.builder()
                    .originalName(file.getOriginalFilename())
                    .saveName(uniqueFileName)
                    .size(getFileSize(file.getSize()))
                    .extension(file.getContentType())
                    .filePath(targetLocation.toString())
                    .build();
        } catch (IOException e) {
            log.error("파일 저장 실패: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("파일 저장 중 오류 발생: " + file.getOriginalFilename(), e);
        }
    }

    /*
     * 파일 삭제
     * */
    public void removeFile(List<Attachment> attachmentList) {
        for (Attachment attachment : attachmentList) {
            File file = new File(attachment.getFilePath());
            if (file.exists()) {
                file.delete();
            }else{
                log.error("파일 삭제 실패: {}", attachment.getFilePath());
            }
        }
    }

    /*
     * 파일 다운로드
     * */
    public ResponseEntity<Resource> downloadFile(List<Attachment> attachmentList) {
        File file = null;
        if(attachmentList.size() == 1){
            Attachment attachment = attachmentList.get(0);
            file = new File(attachment.getFilePath());
            if (!file.exists()) {
                log.error("파일 다운로드 실패: 파일이 존재하지 않습니다. {}", attachment.getFilePath());
            }

            Resource resource = new FileSystemResource(file);
            HttpHeaders headers = new HttpHeaders();
            String fileName = URLEncoder.encode(attachment.getOriginalName(), StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        }else if(attachmentList.size() > 1){
            file = createZipFile(attachmentList);
            Resource resource = new FileSystemResource(file);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"attachments.zip\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        }
        return null;
    }

    /*
     * 파일 다건 다운로드
     * */
    public File createZipFile(List<Attachment> attachments) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path tempZipPath = Files.createTempFile("attachments_" + timestamp, ".zip");
            File zipFile = tempZipPath.toFile();
            zipFile.deleteOnExit();

            try (FileOutputStream fos = new FileOutputStream(zipFile);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {

                for (Attachment attachment : attachments) {
                    File fileToZip = new File(attachment.getFilePath());

                    if (!fileToZip.exists()) {
                        log.warn("ZIP 생성 중 파일 없음: {}", attachment.getFilePath());
                        continue;
                    }

                    try (FileInputStream fis = new FileInputStream(fileToZip)) {
                        ZipEntry zipEntry = new ZipEntry(attachment.getOriginalName());
                        zos.putNextEntry(zipEntry);

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) >= 0) {
                            zos.write(buffer, 0, length);
                        }
                        zos.closeEntry();
                    }
                }
            }
            log.info("ZIP 파일 생성 완료: {}", zipFile.getAbsolutePath());
            return zipFile;

        } catch (IOException e) {
            log.error("ZIP 파일 생성 중 오류 발생", e);
            throw new RuntimeException("ZIP 파일 생성 중 오류 발생", e);
        }
    }

    /*
     * 중복 없는 파일 명 생성
     * */
    private String uniqueFileName(String fileName){
        UUID uuid = UUID.randomUUID();
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("파일명이 비어있거나 null일 수 없습니다.");
        }
        int lastDotIndex = fileName.lastIndexOf(".");

        if (lastDotIndex == -1) {
            return fileName + "_" + uuid;
        }

        String name = fileName.substring(0, lastDotIndex);
        String extension = fileName.substring(lastDotIndex + 1);

        return name + "_" + uuid + "." + extension;
    }

    /*
     * 파일 size 계산
     * */
    private String getFileSize(long size) {
        if (size < 1024) {
            return size + "B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2fKB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2fMB", size / (1024.0 * 1024));
        } else {
            return String.format("%.2fGB", size / (1024.0 * 1024 * 1024));
        }
    }

}
