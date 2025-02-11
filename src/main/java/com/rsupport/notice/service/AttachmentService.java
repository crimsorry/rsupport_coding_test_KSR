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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

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

    /*
     * 첨부파일 업데이트
     * */
    public List<AttachmentResponseDto> uploadAttachment(List<MultipartFile> fileList, List<Long> removeIdList, Notice notice) {
        if(fileList.size() != removeIdList.size()) throw new FailException(ErrorCode.ATTACHMENT_COUNT_MISMATCH);

        List<Attachment> existAttachmentList = getNoticeAttachment(notice.getNoticeId());

        List<Attachment> filesToDelete = existAttachmentList.stream()
                .filter(attachment -> removeIdList.contains(attachment.getAttachmentId()))
                .collect(Collectors.toList());

        if(filesToDelete.size() != removeIdList.size()) throw new FailException(ErrorCode.ATTACHMENT_MISMATCH);

        // 파일 삭제
        fileUtil.removeFile(filesToDelete);
        filesToDelete.forEach(attachment -> attachment.softDelete());

        // 파일 업로드
        List<Attachment> attachmentList = fileUtil.uploadFile(fileList);
        attachmentList.forEach(attachment -> attachment.setNotice(notice));
        List<Attachment> saveAttachmentList = attachmentRepository.storeAll(attachmentList);
        return AttachmentConvertor.toResponseDto(saveAttachmentList);
    }

    /*
     * 첨부파일 삭제
     * */
    @Transactional
    public void deleteAttachment(Long noticeId) {
        List<Attachment> attachmentList = attachmentRepository.findByNoticeId(noticeId);
        fileUtil.removeFile(attachmentList);
        for(Attachment attachment : attachmentList) {
            attachment.softDelete();
        }
    }

}
