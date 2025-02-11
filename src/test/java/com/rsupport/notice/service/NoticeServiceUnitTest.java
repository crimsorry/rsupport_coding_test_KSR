package com.rsupport.notice.service;

import com.rsupport.notice.domain.entity.Notice;
import com.rsupport.notice.domain.repository.NoticeRepository;
import com.rsupport.notice.dto.NoticeConvertor;
import com.rsupport.notice.dto.request.NoticeRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class NoticeServiceUnitTest {

    @InjectMocks
    private NoticeService noticeService;

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache userCache;

    @Mock
    private Cache viewCache;

    @Test
    @DisplayName("공지사항 등록 성공")
    void testAddNotice(){
        // given
        String userName = "test";
        NoticeRequestDto noticeRequestDto = new NoticeRequestDto();
        Notice notice = mock(Notice.class);


    }
}