package com.rsupport.notice.infra.impl;

import com.rsupport.notice.domain.entity.Notice;
import com.rsupport.notice.domain.repository.NoticeRepository;
import com.rsupport.notice.infra.jpa.NoticeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepository {

    private final NoticeJpaRepository repository;

    public Notice store(Notice notice){
        return repository.save(notice);
    }

    @Override
    public Optional<Notice> findById(long noticeId) {
        return repository.findById(noticeId);
    }

    @Override
    public Optional<Notice> findByIdAndIsDeleted(Long noticeId, Boolean isDeleted) {
        return repository.findByNoticeIdAndIsDeleted(noticeId, isDeleted);
    }

    @Override
    public int updateView(Long noticeId, Long viewCount) {
        return repository.updateView(noticeId, viewCount);
    }

}
