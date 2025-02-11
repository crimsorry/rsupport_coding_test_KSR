package com.rsupport.notice.facade;

import com.rsupport.notice.domain.entity.Attachment;
import com.rsupport.notice.domain.entity.Notice;
import com.rsupport.notice.dto.NoticeConvertor;
import com.rsupport.notice.dto.request.NoticeRequestDto;
import com.rsupport.notice.dto.response.AttachmentResponseDto;
import com.rsupport.notice.dto.response.NoticeDetailResponseDto;
import com.rsupport.notice.dto.response.NoticeResponseDto;
import com.rsupport.notice.service.AttachmentService;
import com.rsupport.notice.service.NoticeService;
import com.rsupport.notice.support.component.Facade;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Facade
@Transactional
@RequiredArgsConstructor
public class NoticeFacade {

    private final NoticeService noticeService;
    private final AttachmentService attachmentService;

    public NoticeResponseDto createNoticeWithAttachments(NoticeRequestDto noticeRequestDto, List<MultipartFile> fileList, String userName) {
        Notice notice = noticeService.addNotice(noticeRequestDto, userName);
        List<AttachmentResponseDto> attachmentList = attachmentService.addAttachment(fileList, notice);
        NoticeResponseDto noticeResult = NoticeConvertor.toResponseDto(notice);
        noticeResult.setAttachmentList(attachmentList);
        return noticeResult;
    }

    public NoticeDetailResponseDto getNotice(Long noticeId, String userName){
        NoticeDetailResponseDto noticeDto = noticeService.getDetailNotice(noticeId, userName);
        List<Attachment> attachmentList = attachmentService.getNoticeAttachment(noticeId);
        List<Long> idList = attachmentList.stream()
                .map(Attachment::getAttachmentId)
                .collect(Collectors.toList());
        noticeDto.setAttachmentIdList(idList);
        return noticeDto;
    }

    public NoticeResponseDto updateNotice(Long noticeId, NoticeRequestDto noticeRequestDto, List<MultipartFile> fileList, List<Long> removeIdList, String userName){
        Notice notice = noticeService.updateNotice(noticeId, noticeRequestDto, userName);
        List<AttachmentResponseDto> attachmentList = attachmentService.uploadAttachment(fileList, removeIdList, notice);
        NoticeResponseDto noticeResult = NoticeConvertor.toResponseDto(notice);
        noticeResult.setAttachmentList(attachmentList);
        return noticeResult;
    }

    public void deleteNotice(Long noticeId, String userName){
        noticeService.deleteNotice(noticeId, userName);
        attachmentService.deleteAttachment(noticeId);
    }


}
