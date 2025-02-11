package com.rsupport.notice.infra.impl;

import com.rsupport.notice.domain.entity.Attachment;
import com.rsupport.notice.domain.repository.AttachmentRepository;
import com.rsupport.notice.infra.jpa.AttachmentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AttachmentRepositoryImpl implements AttachmentRepository {

    private final AttachmentJpaRepository repository;

    @Override
    public Attachment store(Attachment attachment) {
        return repository.save(attachment);
    }

    @Override
    public List<Attachment> storeAll(List<Attachment> attachment) {
        return repository.saveAll(attachment);
    }

    @Override
    public Optional<Attachment> findById(Long attachmentId) {
        return repository.findById(attachmentId);
    }

    @Override
    public Optional<Attachment> findByIdAndIsDeleted(Long attachmentId, Boolean isDeleted) {
        return repository.findByAttachmentIdAndIsDeleted(attachmentId, isDeleted);
    }

    @Override
    public List<Attachment> findByNoticeId(Long noticeId) {
        return repository.findByNotice_NoticeId(noticeId);
    }

    @Override
    public List<Attachment> findByNoticeIdAndIsDeleted(Long noticeId, Boolean isDeleted) {
        return repository.findByNotice_NoticeIdAndIsDeleted(noticeId, isDeleted);
    }

    @Override
    public List<Attachment> findAllByAttachmentId(List<Long> attachment) {
        return repository.findAllByAttachmentIdIn(attachment);
    }
}
