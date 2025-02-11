package com.rsupport.notice.controller;

import com.rsupport.notice.dto.AttachmentConvertor;
import com.rsupport.notice.dto.response.AttachmentResponseDto;
import com.rsupport.notice.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
