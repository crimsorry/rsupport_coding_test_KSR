package com.rsupport.notice.util;

import com.rsupport.notice.domain.entity.Attachment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class FileUtilTest {@Autowired
private FileUtil fileUtil;

    @Test
    @DisplayName("파일 업로드 성공")
    void testUploadFileSuccess() {
        // given
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.txt", "text/plain", "RSupport Upload Test".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.txt", "text/plain", "RSupport Upload Test2".getBytes());
        List<MultipartFile> fileList = List.of(file1, file2);

        // when
        List<Attachment> result = fileUtil.uploadFile(fileList);

        // then
        assertNotNull(result);
        assertThat(result.size()).isEqualTo(2);

        for (Attachment attachment : result) {
            File uploadedFile = new File(attachment.getFilePath());
            assertTrue(uploadedFile.exists(), "업로드된 파일이 존재해야 합니다.");
        }
    }

    @Test
    @DisplayName("파일 삭제 성공")
    void testRemoveFileSuccess() {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "delete-test.txt", "text/plain", "RSupport Delete Test".getBytes());
        List<Attachment> attachments = fileUtil.uploadFile(List.of(file));

        assertFalse(attachments.isEmpty());
        File fileToDelete = new File(attachments.get(0).getFilePath());
        assertTrue(fileToDelete.exists(), "삭제할 파일이 존재해야 합니다.");

        // when
        fileUtil.removeFile(attachments);

        // then
        assertFalse(fileToDelete.exists(), "파일이 삭제되어야 합니다.");
    }

    @Test
    @DisplayName("파일 단건 다운로드 성공")
    void testDownloadFileSuccess() {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "download-test.txt", "text/plain", "RSupport Download Test".getBytes());
        List<Attachment> attachments = fileUtil.uploadFile(List.of(file));

        // when
        ResponseEntity<Resource> response = fileUtil.downloadFile(attachments);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("파일 다건 다운로드 성공 - zip 파일")
    void testCreateZipFileSuccess() {
        // given
        MockMultipartFile file1 = new MockMultipartFile("file", "zip-test1.txt", "text/plain", "File 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "zip-test2.txt", "text/plain", "File 2".getBytes());

        List<Attachment> attachments = fileUtil.uploadFile(List.of(file1, file2));

        // when
        File zipFile = fileUtil.createZipFile(attachments);

        // then
        assertNotNull(zipFile);
        assertTrue(zipFile.exists(), "ZIP 파일이 생성되어야 합니다.");
    }

}
