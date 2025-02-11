package com.rsupport.notice.service;

import com.rsupport.notice.core.ErrorCode;
import com.rsupport.notice.domain.entity.Notice;
import com.rsupport.notice.domain.repository.NoticeRepository;
import com.rsupport.notice.dto.NoticeConvertor;
import com.rsupport.notice.dto.request.NoticeRequestDto;
import com.rsupport.notice.support.error.FailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

}
