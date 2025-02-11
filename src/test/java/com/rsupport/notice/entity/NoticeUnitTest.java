package com.rsupport.notice.entity;

import com.rsupport.notice.core.ErrorCode;
import com.rsupport.notice.domain.entity.Notice;
import com.rsupport.notice.support.error.FailException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class NoticeUnitTest {

    private Notice notice;

    @Test
    @DisplayName("개시일이 종료일보다 큰 오류")
    void testValidateOpenDateError(){
        // given
        notice = Notice.builder()
                .startDate(LocalDateTime.now().minusHours(1))
                .endDate(LocalDateTime.now().minusHours(3))
                .build();

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            notice.validateOpenDate();
        });

        assertThat(exception.getMessage()).isEqualTo(ErrorCode.NOTICE_DATE_ORDER_INVALID.getMessage());
    }
    @Test
    @DisplayName("개시일이 종료일보다 작다. 성공")
    void testValidateOpenDate(){
        // given
        notice = Notice.builder()
                .startDate(LocalDateTime.now().minusHours(1))
                .endDate(LocalDateTime.now())
                .build();

        // when & then
        assertDoesNotThrow(notice::validateOpenDate);
    }


}