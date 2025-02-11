package com.rsupport.notice.infra.impl;

import com.rsupport.notice.domain.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepository {

}
