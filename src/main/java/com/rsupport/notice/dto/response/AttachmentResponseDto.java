package com.rsupport.notice.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttachmentResponseDto {

    private Long attachmentId;

    private String originalName;

    private String saveName;

    private String size;

    private String extension;

    private String filePath;


}
