package com.rsupport.notice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.io.File;

@Getter
@Builder
public class AttachmentDownloadDto {

    private File file;

    private String saveName;

    private String size;

    private String extension;

}
