package com.rsupport.notice.service;

import com.rsupport.notice.domain.entity.Notice;
import com.rsupport.notice.domain.repository.NoticeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@SpringBootTest
class NoticeServiceIntegrationTest {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private CacheManager cacheManager;

    @Test
    @DisplayName("조회수 1회 증가 - 새로운 사용자")
    void testIncreaseView_NewUser() {
        // given
        String userName = "testUser";
        Notice notice = Notice.builder().title("test title").content("test content").startDate(LocalDateTime.now()).endDate(LocalDateTime.now().plusDays(1)).writer(userName).build();
        Notice saveNotice = noticeRepository.store(notice);
    }

    @Test
    @DisplayName("조회수 2회 증가 DB 반영 - 유저 2명이 각 2번씩 조회")
    void testSyncViewCountToDB() {
        // given
        String userName = "testUser";
        String userName2 = "testUser2";
        Notice notice = Notice.builder().title("test title").content("test content").startDate(LocalDateTime.now()).endDate(LocalDateTime.now().plusDays(1)).writer(userName).build();
        noticeRepository.store(notice);
    }
}
