package com.rsupport.notice.domain.repository;

import com.rsupport.notice.domain.entity.Attachment;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository {

    Attachment store(Attachment attachment);
    List<Attachment> storeAll(List<Attachment> attachment);
    Optional<Attachment> findById(Long attachmentId);
    Optional<Attachment> findByIdAndIsDeleted(Long attachmentId, Boolean isDeleted);
    List<Attachment> findByNoticeId(Long noticeId);
    List<Attachment> findByNoticeIdAndIsDeleted(Long noticeId, Boolean isDeleted);
    List<Attachment> findAllByAttachmentId(List<Long> attachment);

}
