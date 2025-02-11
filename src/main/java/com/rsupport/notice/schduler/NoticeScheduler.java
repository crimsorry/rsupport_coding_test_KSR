package com.rsupport.notice.schduler;

import com.rsupport.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class NoticeScheduler {

    private final NoticeService noticeService;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void disableExpiredQuote() {
        log.info("# [NoticeScheduler] ::: Scheduler Start");
        noticeService.syncViewCountToDB();
        log.info("# [NoticeScheduler] ::: Scheduler End");
    }

}
