package com.rsupport.notice.service;

import com.rsupport.notice.core.ErrorCode;
import com.rsupport.notice.domain.entity.Notice;
import com.rsupport.notice.domain.repository.NoticeRepository;
import com.rsupport.notice.dto.NoticeConvertor;
import com.rsupport.notice.dto.request.NoticeRequestDto;
import com.rsupport.notice.dto.response.NoticeDetailResponseDto;
import com.rsupport.notice.support.error.FailException;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        try (MockedStatic<NoticeConvertor> mockConvertor = mockStatic(NoticeConvertor.class)) {
            // when
            mockConvertor.when(() -> NoticeConvertor.toEntity(noticeRequestDto, userName)).thenReturn(notice);
            when(noticeRepository.store(notice)).thenReturn(notice);

            // then
            Notice result = noticeService.addNotice(noticeRequestDto, userName);

            assertNotNull(result);
            mockConvertor.verify(() -> NoticeConvertor.toEntity(noticeRequestDto, userName), times(1));
            verify(noticeRepository, times(1)).store(notice);
        }
    }

    @Test
    @DisplayName("존재하지 않는 공지사항 정보")
    void testGetNoticeFail(){
        // given
        Long noticeId = 1L;
        Boolean isDeleted = Boolean.FALSE;

        when(noticeRepository.findByIdAndIsDeleted(noticeId, isDeleted)).thenReturn(Optional.empty());

        // when
        Exception exception = assertThrows(FailException.class, () -> {
            noticeService.getNotice(noticeId);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.NOTICE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("공지사항 정보 조회 성공")
    void testGetNoticeSuccess(){
        // given
        Long noticeId = 1L;
        String title = "공지사항 제목";
        String content = "공지사항 내용";
        String userName = "test";
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        Boolean isDeleted = Boolean.FALSE;
        Notice notice = mock(Notice.class);

        when(notice.getTitle()).thenReturn(title);
        when(notice.getContent()).thenReturn(content);
        when(notice.getWriter()).thenReturn(userName);
        when(notice.getStartDate()).thenReturn(startDate);
        when(notice.getEndDate()).thenReturn(endDate);
        when(noticeRepository.findByIdAndIsDeleted(noticeId, isDeleted)).thenReturn(Optional.ofNullable(notice));

        // when
        Notice result = noticeService.getNotice(noticeId);

        // then
        verify(noticeRepository, times(1)).findByIdAndIsDeleted(noticeId, isDeleted);
        assertThat(result.getTitle()).isEqualTo(notice.getTitle());
        assertThat(result.getContent()).isEqualTo(notice.getContent());
        assertThat(result.getWriter()).isEqualTo(notice.getWriter());
        assertThat(result.getStartDate()).isEqualTo(notice.getStartDate());
        assertThat(result.getEndDate()).isEqualTo(notice.getEndDate());
        assertThat(result.getIsDeleted()).isEqualTo(isDeleted);
    }

    @Test
    @DisplayName("공지사항 상세 조회 성공")
    void testGetNoticeDetailSuccess(){
        // given
        Long noticeId = 1L;
        Long count = 0L;
        String title = "공지사항 제목";
        String content = "공지사항 내용";
        String userName = "test";
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        Boolean isDeleted = Boolean.FALSE;
        Notice notice = mock(Notice.class);

        when(notice.getTitle()).thenReturn(title);
        when(notice.getContent()).thenReturn(content);
        when(notice.getWriter()).thenReturn(userName);
        when(notice.getStartDate()).thenReturn(startDate);
        when(notice.getEndDate()).thenReturn(endDate);
        when(notice.getNoticeId()).thenReturn(1L);
        when(userCache.get(userName + "_" + noticeId)).thenReturn(null);
        when(viewCache.get(noticeId, Long.class)).thenReturn(count);
        when(cacheManager.getCache("notice_view_user_cache")).thenReturn(userCache);
        when(cacheManager.getCache("notice_view_cache")).thenReturn(viewCache);
        when(noticeRepository.findByIdAndIsDeleted(noticeId, isDeleted)).thenReturn(Optional.ofNullable(notice));

        // when
        NoticeDetailResponseDto result = noticeService.getDetailNotice(noticeId, userName);

        // then
        verify(noticeRepository, times(1)).findByIdAndIsDeleted(noticeId, isDeleted);
        assertThat(result.getTitle()).isEqualTo(notice.getTitle());
        assertThat(result.getContent()).isEqualTo(notice.getContent());
        assertThat(result.getWriter()).isEqualTo(notice.getWriter());
        assertThat(result.getStartDate()).isEqualTo(notice.getStartDate());
        assertThat(result.getEndDate()).isEqualTo(notice.getEndDate());
    }

    @Test
    @DisplayName("조회수 증가 성공")
    void testIncreaseViewSuccess() {
        // given
        Long noticeId = 1L;
        String userName = "test";
        Long count = 0L;

        when(userCache.get(userName + "_" + noticeId)).thenReturn(null);
        when(viewCache.get(noticeId, Long.class)).thenReturn(count);
        when(cacheManager.getCache("notice_view_user_cache")).thenReturn(userCache);
        when(cacheManager.getCache("notice_view_cache")).thenReturn(viewCache);

        // when
        Long updatedView = noticeService.increaseView(noticeId, userName);

        // then
        assertThat(updatedView).isEqualTo(count + 1);
        verify(userCache, times(1)).put(userName + "_" + noticeId, true);
        verify(viewCache, times(1)).put(noticeId, count + 1);
    }

    @Test
    @DisplayName("조회수 증가 실패 - 유저 이미 존재")
    void testIncreaseViewFail() {
        // given
        Long noticeId = 1L;
        String userName = "test";
        Long count = 0L;
        when(userCache.get(userName + "_" + noticeId)).thenReturn(() -> true);
        when(cacheManager.getCache("notice_view_user_cache")).thenReturn(userCache);
        when(cacheManager.getCache("notice_view_cache")).thenReturn(viewCache);

        // when & then
        Long updatedView = noticeService.increaseView(noticeId, userName);

        assertThat(updatedView).isEqualTo(count);
        verify(userCache, never()).put(eq(userName + "_" + noticeId), any());
        verify(viewCache, never()).put(eq(noticeId), any());
    }
}