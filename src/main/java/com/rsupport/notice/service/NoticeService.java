package com.rsupport.notice.service;

import com.rsupport.notice.core.ErrorCode;
import com.rsupport.notice.domain.entity.Notice;
import com.rsupport.notice.domain.repository.NoticeRepository;
import com.rsupport.notice.dto.NoticeConvertor;
import com.rsupport.notice.dto.request.NoticeRequestDto;
import com.rsupport.notice.dto.response.NoticeDetailResponseDto;
import com.rsupport.notice.support.error.FailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final CacheManager cacheManager;

    private long totalView = 0L;
    private final Map<Long, Long> viewBuffer = new ConcurrentHashMap<>(); // 조회수 임시 저장 버퍼

    /*
     * 공지사항 등록
     * */
    @Transactional
    public Notice addNotice(NoticeRequestDto noticeRequestDto, String userName){
        Notice notice = NoticeConvertor.toEntity(noticeRequestDto, userName);
        notice.validateOpenDate();
        return noticeRepository.store(notice);
    }

    /*
     * 공지사항 단건 조회
     * */
    public Notice getNotice(Long noticeId){
        return noticeRepository.findByIdAndIsDeleted(noticeId, Boolean.FALSE).orElseThrow(() -> new FailException(ErrorCode.NOTICE_NOT_FOUND));
    }

    /*
     * 공지사항 상세 조회
     * */
    public NoticeDetailResponseDto getDetailNotice(Long noticeId, String userName) {
        Notice notice = getNotice(noticeId);
        increaseView(notice.getNoticeId(), userName);
        return NoticeConvertor.toDetailResponseDto(notice, notice.getView() + totalView);
    }

    /**
     * 조회수 증가 (5분 동안 같은 유저 중복 방지)
     */
    public Long increaseView(Long noticeId, String userId) {
        Cache userCache = cacheManager.getCache("notice_view_user_cache");
        Cache viewCache = cacheManager.getCache("notice_view_cache");

        if (userCache == null || viewCache == null) {
            throw new IllegalStateException("CacheManager에서 캐시를 찾을 수 없습니다.");
        }

        String userKey = userId + "_" + noticeId;

        if (userCache.get(userKey) != null) {
            return totalView; // 같은 유저가 5분 내 다시 조회하면 증가 안 함
        }

        userCache.put(userKey, true); // 5분 동안 조회 제한

        // 조회수 증가 (캐시에 저장)
        Long currentCount = viewCache.get(noticeId, Long.class);
        if (currentCount == null) {
            currentCount = 0L;
        }

        currentCount += 1;
        totalView += 1;
        viewCache.put(noticeId, currentCount);
        viewBuffer.merge(noticeId, 1L, Long::sum);
        return totalView;
    }

    /**
     * 1분마다 조회수를 DB에 반영
     */
    @Transactional
    public void syncViewCountToDB() {
        if (viewBuffer.isEmpty()) return;

        for (Map.Entry<Long, Long> entry : viewBuffer.entrySet()) {
            noticeRepository.updateView(entry.getKey(), entry.getValue());
        }

        viewBuffer.clear();
        totalView = 0L;
    }

    /*
     * 공지사항 업데이트
     * */
    @Transactional
    public Notice updateNotice(Long noticeId, NoticeRequestDto noticeRequestDto, String userName){
        Notice notice = getNotice(noticeId);
        notice.updateContent(noticeRequestDto.getTitle(), noticeRequestDto.getContent(), noticeRequestDto.getStartDate(), noticeRequestDto.getEndDate(), userName);
        notice.validateOpenDate();
        return notice;
    }

    /*
     * 공지사항 삭제
     * */
    @Transactional
    public void deleteNotice(Long noticeId, String userName){
        Notice notice = getNotice(noticeId);
        notice.softDelete(userName);
    }

}
