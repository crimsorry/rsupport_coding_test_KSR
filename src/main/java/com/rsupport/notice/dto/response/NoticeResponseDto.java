package com.rsupport.notice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class NoticeResponseDto {

    private Long noticeId;

    private String title;

    private String content;

    private Long view;

    private String writer;

    private String modifiedWriter;

    private String deleteWriter;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private List<AttachmentResponseDto> attachmentList;

    public void setAttachmentList(List<AttachmentResponseDto> attachmentList) {
        this.attachmentList = attachmentList;
    }

}
