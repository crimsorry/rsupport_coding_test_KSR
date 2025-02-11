package com.rsupport.notice.service;

import com.rsupport.notice.core.ErrorCode;
import com.rsupport.notice.domain.entity.Attachment;
import com.rsupport.notice.domain.entity.Notice;
import com.rsupport.notice.domain.repository.AttachmentRepository;
import com.rsupport.notice.dto.AttachmentConvertor;
import com.rsupport.notice.dto.response.AttachmentResponseDto;
import com.rsupport.notice.support.error.FailException;
import com.rsupport.notice.util.FileUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttachmentServiceUnitTest {

    @InjectMocks
    private AttachmentService attachmentService;

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private FileUtil fileUtil;

    @Mock
    private MultipartFile multipartFile;

    private List<MultipartFile> fileList;
    private List<Long> removeIdList;
    private List<Attachment> uploadedAttachments;

    @Test
    @DisplayName("첨부파일 등록 성공")
    void testAddAttachment(){
        // given
        List<MultipartFile> fileList = List.of(multipartFile);
        List<Attachment> mockAttachments = List.of(Attachment.builder().build());
        List<AttachmentResponseDto> expectedResponse = List.of(AttachmentResponseDto.builder().build());
        Notice notice = Notice.builder().build();

        when(fileUtil.uploadFile(fileList)).thenReturn(mockAttachments);
        when(attachmentRepository.storeAll(mockAttachments)).thenReturn(mockAttachments);
        mockStatic(AttachmentConvertor.class);
        when(AttachmentConvertor.toResponseDto(mockAttachments)).thenReturn(expectedResponse);

        // when
        List<AttachmentResponseDto> result = attachmentService.addAttachment(fileList, notice);

        // then
        assertNotNull(result);
        assertThat(result.size()).isEqualTo(expectedResponse.size());
        verify(fileUtil, times(1)).uploadFile(fileList);
        verify(attachmentRepository, times(1)).storeAll(mockAttachments);
    }

    @Test
    @DisplayName("존재하지 않는 첨부파일 정보")
    void testGetAttachmentFail(){
        // given
        Long attachmentId = 1L;
        Boolean isDeleted = Boolean.FALSE;

        when(attachmentRepository.findByIdAndIsDeleted(attachmentId, isDeleted)).thenReturn(Optional.empty());

        // when
        Exception exception = assertThrows(FailException.class, () -> {
            attachmentService.getAttachment(attachmentId);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.ATTACHMENT_DATE_ORDER_INVALID.getMessage());
    }

    @Test
    @DisplayName("첨부파일 정보 조회 성공")
    void testGetAttachmentSuccess(){
        // given
        Long attachmentId = 1L;
        Boolean isDeleted = Boolean.FALSE;
        Attachment attachment = Attachment.builder().build();

        when(attachmentRepository.findByIdAndIsDeleted(attachmentId, isDeleted)).thenReturn(Optional.ofNullable(attachment));

        // when
        attachmentService.getAttachment(attachmentId);

        // then
        verify(attachmentRepository, times(1)).findByIdAndIsDeleted(attachmentId, isDeleted);
    }

    @Test
    @DisplayName("첨부파일 전체 조회 성공")
    void testGetNoticeAttachment(){
        // given
        Long noticeId = 1L;
        Boolean isDeleted = Boolean.FALSE;
        List<Attachment> attachmentList = List.of(Attachment.builder().build());

        when(attachmentRepository.findByNoticeIdAndIsDeleted(noticeId, isDeleted)).thenReturn(attachmentList);

        // when
        attachmentService.getNoticeAttachment(noticeId);

        // then
        verify(attachmentRepository, times(1)).findByNoticeIdAndIsDeleted(noticeId, isDeleted);
    }
}