package com.rsupport.notice.infra.jpa;

import com.rsupport.notice.domain.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeJpaRepository extends JpaRepository<Notice, Long> {

}
