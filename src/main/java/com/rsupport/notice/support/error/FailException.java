package com.rsupport.notice.support.error;

import com.rsupport.notice.core.ErrorCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.logging.LogLevel;

@Getter
@ToString
public class FailException extends RuntimeException {

    private final ErrorCode errorCode;
    private final LogLevel logLevel;

    public FailException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.logLevel = LogLevel.INFO;
    }

    public FailException(ErrorCode errorCode, LogLevel logLevel) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.logLevel = logLevel;
    }

}