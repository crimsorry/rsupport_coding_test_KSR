package com.rsupport.notice.infra.jpa;

import com.rsupport.notice.domain.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentJpaRepository extends JpaRepository<Attachment, Long> {

}
