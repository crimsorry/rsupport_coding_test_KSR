package com.rsupport.notice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AttachmentRequestDto {

    private String originalName;

    private String saveName;

    private Long size;

    private String extension;

    private String filePath;

}
