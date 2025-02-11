package com.rsupport.notice.domain.repository;

import com.rsupport.notice.domain.entity.Notice;

import java.util.Optional;

public interface NoticeRepository {

    Notice store(Notice notice);
    Optional<Notice> findById(long noticeId);
    Optional<Notice> findByIdAndIsDeleted(Long noticeId, Boolean isDeleted);
    int updateView(Long noticeId, Long viewCount);

}
