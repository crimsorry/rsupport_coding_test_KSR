package com.rsupport.notice.controller;

import com.rsupport.notice.dto.request.NoticeRequestDto;
import com.rsupport.notice.dto.response.NoticeDetailResponseDto;
import com.rsupport.notice.dto.response.NoticeResponseDto;
import com.rsupport.notice.facade.NoticeFacade;
import com.rsupport.notice.support.constructor.NoticeConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeFacade noticeFacade;

    /*
     * 공지사항 + 첨부파일 등록
     * */
    @PostMapping(value = "", consumes = { "multipart/form-data" })
    public ResponseEntity<NoticeResponseDto> postNotice(
            @RequestHeader(NoticeConstants.HEADER_USER_NAME) String userName,
            @RequestPart("notice") @Valid NoticeRequestDto notice,
            @RequestPart(value = "file", required = false) List<MultipartFile> fileList
    ) {
        NoticeResponseDto noticeResult = noticeFacade.createNoticeWithAttachments(notice, fileList, userName);
        return ResponseEntity.ok().body(noticeResult);
    }

    /*
     * 공지사항 + 첨부파일Id 조회
     * */
    @GetMapping(value = "/{noticeId}", produces = "application/json")
    public ResponseEntity<NoticeDetailResponseDto> getNotice(
            @RequestHeader(NoticeConstants.HEADER_USER_NAME) String userName,
            @PathVariable("noticeId") long noticeId
    ) {
        NoticeDetailResponseDto noticeResult = noticeFacade.getNotice(noticeId, userName);
        return ResponseEntity.ok().body(noticeResult);
    }

    /*
     * 공지사항 + 첨부파일 업데이트
     * */
    @PutMapping(value = "/{noticeId}", consumes = { "multipart/form-data" })
    public ResponseEntity<NoticeResponseDto> updateNotice(
            @RequestHeader(NoticeConstants.HEADER_USER_NAME) String userName,
            @PathVariable("noticeId") long noticeId,
            @RequestPart("notice") @Valid NoticeRequestDto notice,
            @RequestPart("removeIdList") @Valid List<Long> removeIdList,
            @RequestPart(value = "file", required = false) List<MultipartFile> fileList
    ) {
        NoticeResponseDto noticeResult = noticeFacade.updateNotice(noticeId, notice, fileList,  removeIdList, userName);
        return ResponseEntity.ok().body(noticeResult);
    }

    /*
     * 공지사항 + 첨부파일 삭제
     * */
    @DeleteMapping(value = "/{noticeId}", produces = "application/json")
    public ResponseEntity<?> deleteNotice(
            @RequestHeader(NoticeConstants.HEADER_USER_NAME) String userName,
            @PathVariable("noticeId") long noticeId
    ) {
        noticeFacade.deleteNotice(noticeId, userName);
        return ResponseEntity.ok().body(null);
    }

}
