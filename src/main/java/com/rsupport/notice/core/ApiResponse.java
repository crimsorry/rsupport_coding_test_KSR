package com.rsupport.notice.core;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse {

    int resultCode;
    String resultMsg;

    public ApiResponse(int resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public static ApiResponse success() {
        return ApiResponse.builder()
                .resultCode(200)
                .resultMsg("OK")
                .build();
    }

    public static ApiResponse fail(int resultCode, String resultMsg) {
        return ApiResponse.builder()
                .resultCode(resultCode)
                .resultMsg(resultMsg)
                .build();
    }
}