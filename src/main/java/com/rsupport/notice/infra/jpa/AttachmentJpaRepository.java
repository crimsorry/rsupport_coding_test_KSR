package com.rsupport.notice.infra.jpa;

import com.rsupport.notice.domain.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttachmentJpaRepository extends JpaRepository<Attachment, Long> {

    Optional<Attachment> findByAttachmentIdAndIsDeleted(Long attachmentId, Boolean isDeleted);
    List<Attachment> findByNotice_NoticeId(Long noticeId);
    List<Attachment> findAllByAttachmentIdIn(List<Long> attachment);
    List<Attachment> findByNotice_NoticeIdAndIsDeleted(Long noticeId, Boolean isDeleted);

}
