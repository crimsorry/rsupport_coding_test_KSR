package com.rsupport.notice.core;

import lombok.Getter;

@Getter
public enum ErrorCode {

    NOTICE_NOT_FOUND("존재하지 않는 공지사항입니다."),
    NOTICE_DATE_ORDER_INVALID("개시일은 종료일보다 작아야 합니다."),

    ATTACHMENT_DATE_ORDER_INVALID("존재하지 않는 첨부파일입니다.."),
    ATTACHMENT_MISMATCH("삭제하려는 첨부파일이 원본에 존재하지 않습니다."),
    ATTACHMENT_COUNT_MISMATCH("삭제 첨부파일과 업데이트할 첨부파일 수가 일치하지 않습니다.");


    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}