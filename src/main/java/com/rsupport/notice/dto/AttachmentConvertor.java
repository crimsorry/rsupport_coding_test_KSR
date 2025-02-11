package com.rsupport.notice.dto;

import com.rsupport.notice.domain.entity.Attachment;
import com.rsupport.notice.dto.response.AttachmentResponseDto;

import java.util.List;

public class AttachmentConvertor {

    public static AttachmentResponseDto toResponseDto(Attachment attachment) {
        return AttachmentResponseDto.builder()
                .attachmentId(attachment.getAttachmentId())
                .originalName(attachment.getOriginalName())
                .saveName(attachment.getSaveName())
                .size(attachment.getSize())
                .extension(attachment.getExtension())
                .filePath(attachment.getFilePath())
                .build();
    }

    public static List<AttachmentResponseDto> toResponseDto(List<Attachment> attachment) {
        return attachment.stream()
                .map(AttachmentConvertor::toResponseDto)
                .toList();
    }

}