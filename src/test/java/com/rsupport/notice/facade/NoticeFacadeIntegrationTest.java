package com.rsupport.notice.facade;

import com.rsupport.notice.domain.entity.Attachment;
import com.rsupport.notice.domain.entity.Notice;
import com.rsupport.notice.domain.repository.AttachmentRepository;
import com.rsupport.notice.domain.repository.NoticeRepository;
import com.rsupport.notice.dto.request.NoticeRequestDto;
import com.rsupport.notice.dto.response.NoticeResponseDto;
import com.rsupport.notice.service.AttachmentService;
import com.rsupport.notice.service.NoticeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class NoticeFacadeIntegrationTest {

    @Autowired
    private NoticeFacade noticeFacade;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Test
    @DisplayName("공지사항 등록 성공")
    void testCreateNoticeWithAttachments() {
        // given
        String userName = "testUser";
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("test title")
                .content("test content")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .build();
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.txt", "text/plain", "RSupport Upload Test".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.txt", "text/plain", "RSupport Upload Test2".getBytes());
        List<MultipartFile> fileList = List.of(file1, file2);

        // when

        // then
    }
}
