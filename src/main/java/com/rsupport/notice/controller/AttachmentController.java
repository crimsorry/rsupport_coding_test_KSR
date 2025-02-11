package com.rsupport.notice.controller;

import com.rsupport.notice.dto.AttachmentConvertor;
import com.rsupport.notice.dto.response.AttachmentResponseDto;
import com.rsupport.notice.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/attachment")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    /*
     * 첨부파일 단건 조회
     * */
    @GetMapping(value = "/{attachmentId}", produces = "application/json")
    public ResponseEntity<?> getAttachment(
            @PathVariable("attachmentId") long attachmentId
    ) {
        AttachmentResponseDto result =  AttachmentConvertor.toResponseDto(attachmentService.getAttachment(attachmentId));
        return ResponseEntity.ok().body(result);
    }

    /*
     * 첨부파일 단건, 다건 다운로드
     * */
    @GetMapping(value = "/download", produces = "application/octet-stream")
    public ResponseEntity<Resource> downloadNotice(
            @RequestParam("attachmentIdList") List<Long> attachmentIdList
    ) throws IOException {
        return attachmentService.downloadAttachment(attachmentIdList);
    }

}
