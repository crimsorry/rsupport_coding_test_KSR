package com.rsupport.notice.service;

import com.rsupport.notice.core.ErrorCode;
import com.rsupport.notice.domain.entity.Attachment;
import com.rsupport.notice.domain.entity.Notice;
import com.rsupport.notice.domain.repository.AttachmentRepository;
import com.rsupport.notice.dto.AttachmentConvertor;
import com.rsupport.notice.dto.response.AttachmentResponseDto;
import com.rsupport.notice.support.error.FailException;
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

    /*
     * 첨부파일 단건 조회
     * */
    public Attachment getAttachment(Long attachmentId) {
        return attachmentRepository.findByIdAndIsDeleted(attachmentId, Boolean.FALSE).orElseThrow(() -> new FailException(ErrorCode.ATTACHMENT_DATE_ORDER_INVALID));
    }

    /*
     * 첨부파일 전체 조회
     * */
    public List<Attachment> getNoticeAttachment(Long noticeId) {
        return attachmentRepository.findByNoticeIdAndIsDeleted(noticeId, Boolean.FALSE);
    }

}
