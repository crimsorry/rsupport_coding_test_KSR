package com.rsupport.notice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class NoticeDetailResponseDto {

    private Long noticeId;

    private String title;

    private String content;

    private String writer;

    private Long view;

    private String modifiedWriter;

    private String deleteWriter;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private List<Long> attachmentIdList;

    public void setAttachmentIdList(List<Long> attachmentIdList) {
        this.attachmentIdList = attachmentIdList;
    }

}
