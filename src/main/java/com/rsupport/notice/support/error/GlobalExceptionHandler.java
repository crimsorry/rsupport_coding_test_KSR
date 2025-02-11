package com.rsupport.notice.support.error;

import com.rsupport.notice.core.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    /**
     * 404 Not Found 처리
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNotFound(NoHandlerFoundException e) {
        String message = "URL: " + e.getRequestURL() + " 을 찾을 수 없습니다.";
        log.error(message, e);
        return new ResponseEntity<>(ApiResponse.fail(HttpStatus.NOT_FOUND.value(), message), HttpStatus.NOT_FOUND);
    }

    /**
     * 파라미터 누락 처리
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException e) {
        String message = "파라미터 누락: " + e.getParameterName();
        log.error(message, e);
        return new ResponseEntity<>(ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
    }

    /**
     * 유효성 검증 실패 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
    String defaultMessage = e.getBindingResult()
                .getFieldError()
                .getDefaultMessage();
        String message = "유효성 검증 실패: " + defaultMessage;
        log.error(message, e);
        return new ResponseEntity<>(ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
    }

    /**
     * JSON 파싱 오류 처리
     */
    @ExceptionHandler({com.fasterxml.jackson.core.JsonParseException.class, org.springframework.http.converter.HttpMessageNotReadableException.class})
    public ResponseEntity<?> handleJsonParsingException(Exception e) {
        String message = "JSON 파싱 오류: " + e.getMessage();
        log.error(message, e);
        return new ResponseEntity<>(ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
    }

    /**
     * 500 Internal Server Error 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception e) {
        log.error(e);
        return new ResponseEntity<>(ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * ErrorCode Java Exception
     */
    @ExceptionHandler(FailException.class)
    public ResponseEntity<?> handleCustomPointException(FailException e) {
        switch (e.getLogLevel()){
            case ERROR -> log.error("FailException : {}", e.getMessage(), e);
            case WARN -> log.warn("FailException : {}", e.getMessage(), e);
            default -> log.info("FailException : {}", e.getMessage(), e);
        }
        return new ResponseEntity<>(ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}