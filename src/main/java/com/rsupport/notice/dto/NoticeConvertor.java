package com.rsupport.notice.dto;

import com.rsupport.notice.domain.entity.Notice;
import com.rsupport.notice.dto.request.NoticeRequestDto;
import com.rsupport.notice.dto.response.NoticeDetailResponseDto;
import com.rsupport.notice.dto.response.NoticeResponseDto;
import org.springframework.stereotype.Component;

@Component
public class NoticeConvertor {

    public static Notice toEntity(NoticeRequestDto noticeRequestDto, String userName){
        return Notice.builder()
                .title(noticeRequestDto.getTitle())
                .content(noticeRequestDto.getContent())
                .writer(userName)
                .startDate(noticeRequestDto.getStartDate())
                .endDate(noticeRequestDto.getEndDate())
                .build();
    }

    public static NoticeResponseDto toResponseDto(Notice notice){
        return NoticeResponseDto.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .view(notice.getView())
                .writer(notice.getWriter())
                .modifiedWriter(notice.getModifiedWriter())
                .startDate(notice.getStartDate())
                .endDate(notice.getEndDate())
                .createAt(notice.getCreatedAt())
                .updateAt(notice.getUpdatedAt())
                .build();
    }

    public static NoticeDetailResponseDto toDetailResponseDto(Notice notice, Long view){
        return NoticeDetailResponseDto.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .view(view)
                .writer(notice.getWriter())
                .startDate(notice.getStartDate())
                .endDate(notice.getEndDate())
                .createAt(notice.getCreatedAt())
                .updateAt(notice.getUpdatedAt())
                .build();
    }

}
