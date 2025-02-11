package com.rsupport.notice.infra.jpa;

import com.rsupport.notice.domain.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface NoticeJpaRepository extends JpaRepository<Notice, Long> {

    Optional<Notice> findByNoticeIdAndIsDeleted(Long NoticeId, Boolean isDeleted);

    @Modifying
    @Transactional
    @Query("UPDATE Notice n SET n.view = n.view + :viewCount WHERE n.noticeId = :noticeId")
    int updateView(@Param("noticeId") Long noticeId, @Param("viewCount") Long viewCount);

}
