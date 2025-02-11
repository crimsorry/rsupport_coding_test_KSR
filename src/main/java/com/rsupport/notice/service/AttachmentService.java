package com.rsupport.notice.service;

import com.rsupport.notice.domain.entity.Attachment;
import com.rsupport.notice.domain.entity.Notice;
import com.rsupport.notice.domain.repository.AttachmentRepository;
import com.rsupport.notice.dto.AttachmentConvertor;
import com.rsupport.notice.dto.response.AttachmentResponseDto;
import com.rsupport.notice.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final FileUtil fileUtil;

    /*
     * 첨부파일 등록
     * */
    public List<AttachmentResponseDto> addAttachment(List<MultipartFile> fileList, Notice notice) {
        List<Attachment> attachmentList = fileUtil.uploadFile(fileList);
        attachmentList.forEach(attachment -> attachment.setNotice(notice));
        List<Attachment> saveAttachmentList = attachmentRepository.storeAll(attachmentList);
        return AttachmentConvertor.toResponseDto(saveAttachmentList);
    }

}
