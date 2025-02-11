package com.rsupport.notice.facade;

import com.rsupport.notice.core.ErrorCode;
import com.rsupport.notice.domain.entity.Attachment;
import com.rsupport.notice.domain.entity.Notice;
import com.rsupport.notice.domain.repository.AttachmentRepository;
import com.rsupport.notice.domain.repository.NoticeRepository;
import com.rsupport.notice.dto.request.NoticeRequestDto;
import com.rsupport.notice.dto.response.NoticeDetailResponseDto;
import com.rsupport.notice.dto.response.NoticeResponseDto;
import com.rsupport.notice.service.AttachmentService;
import com.rsupport.notice.service.NoticeService;
import com.rsupport.notice.support.error.FailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

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
        NoticeResponseDto result = noticeFacade.createNoticeWithAttachments(noticeRequestDto, fileList, userName);

        Notice savedNotices = noticeService.getNotice(result.getNoticeId());
        List<Attachment> savedAttachment = attachmentService.getNoticeAttachment(result.getNoticeId());

        // then
        assertNotNull(result);
        assertThat(savedNotices.getTitle()).isEqualTo(noticeRequestDto.getTitle());
        assertThat(savedNotices.getContent()).isEqualTo(noticeRequestDto.getContent());
        assertThat(savedNotices.getWriter()).isEqualTo(userName);
        assertThat(savedNotices.getView()).isEqualTo(0L);
        assertThat(savedNotices.getStartDate()).isEqualTo(noticeRequestDto.getStartDate());
        assertThat(savedNotices.getEndDate()).isEqualTo(noticeRequestDto.getEndDate());
        assertThat(fileList.size()).isEqualTo(savedAttachment.size());
        assertThat(savedAttachment.get(0).getOriginalName()).isEqualTo(savedAttachment.get(0).getOriginalName());
        assertThat(savedAttachment.get(0).getSaveName()).isEqualTo(savedAttachment.get(0).getSaveName());
        assertThat(savedAttachment.get(0).getSize()).isEqualTo(savedAttachment.get(0).getSize());
        assertThat(savedAttachment.get(0).getExtension()).isEqualTo(savedAttachment.get(0).getExtension());
        assertThat(savedAttachment.get(0).getFilePath()).isEqualTo(savedAttachment.get(0).getFilePath());
        assertThat(savedAttachment.get(1).getOriginalName()).isEqualTo(savedAttachment.get(1).getOriginalName());
        assertThat(savedAttachment.get(1).getSaveName()).isEqualTo(savedAttachment.get(1).getSaveName());
        assertThat(savedAttachment.get(1).getSize()).isEqualTo(savedAttachment.get(1).getSize());
        assertThat(savedAttachment.get(1).getExtension()).isEqualTo(savedAttachment.get(1).getExtension());
        assertThat(savedAttachment.get(1).getFilePath()).isEqualTo(savedAttachment.get(1).getFilePath());
    }

    @Test
    @DisplayName("공지사항 조회 성공")
    void testGetNotice() {
        // given
        String userName = "testUser";
        String title = "test title";
        Notice saveNotice = Notice.builder().title(title).content("test content").startDate(LocalDateTime.now()).endDate(LocalDateTime.now().plusDays(1)).writer(userName).build();
        Notice notice = noticeRepository.store(saveNotice);
        Attachment saveAttachment = Attachment.builder().originalName("oldFile.txt").saveName("newFile.txt").extension("txt").size("12B").filePath("C:/rsupport/newFIle.txt").notice(notice).build();
        attachmentRepository.store(saveAttachment);

        // when
        NoticeDetailResponseDto result = noticeFacade.getNotice(notice.getNoticeId(), userName);

        // then
        assertNotNull(result);
        assertThat(result.getTitle()).isEqualTo(saveNotice.getTitle());
        assertThat(result.getContent()).isEqualTo(saveNotice.getContent());
        assertThat(result.getWriter()).isEqualTo(userName);
        assertThat(result.getView()).isEqualTo(1L);
        assertThat(result.getStartDate()).isEqualTo(saveNotice.getStartDate());
        assertThat(result.getEndDate()).isEqualTo(saveNotice.getEndDate());
        assertThat(result.getAttachmentIdList().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("공지사항 수정 성공")
    void testUpdateNotice() {
        // given
        String title = "test title";
        String newTitle = "new title";
        String content= "test content";
        String newContent = "new content";
        String userName = "testUser";
        Notice saveNotice = Notice.builder().title(title).content(content).startDate(LocalDateTime.now()).endDate(LocalDateTime.now().plusDays(1)).writer(userName).build();
        Notice notice = noticeRepository.store(saveNotice);
        Attachment saveAttachment = Attachment.builder().originalName("File.txt").saveName("FileReal.txt").extension("txt").size("12B").filePath("C:/rsupport/newFIle.txt").notice(notice).build();
        Attachment attachment = attachmentRepository.store(saveAttachment);
        NoticeRequestDto updateRequestDto = NoticeRequestDto.builder().title(newTitle).content(newContent).startDate(LocalDateTime.now()).endDate(LocalDateTime.now().plusDays(1)).build();
        MockMultipartFile newFile = new MockMultipartFile("file", "newFile.txt", "text/plain", "Updated File".getBytes());
        List<MultipartFile> newFileList = List.of(newFile);
        List<Long> removeIdList = List.of(attachment.getAttachmentId());

        // when
        NoticeResponseDto result = noticeFacade.updateNotice(notice.getNoticeId(), updateRequestDto, newFileList, removeIdList, userName);
        Notice resultNotices = noticeService.getNotice(result.getNoticeId());
        Attachment deleteAttachment = attachmentRepository.findById(attachment.getAttachmentId()).get();
        Attachment resultAttachment = attachmentRepository.findById(result.getAttachmentList().get(0).getAttachmentId()).get();

        // then
        assertNotNull(result);
        assertTrue(deleteAttachment.getIsDeleted());
        assertThat(resultNotices.getTitle()).isEqualTo(saveNotice.getTitle());
        assertThat(resultNotices.getContent()).isEqualTo(saveNotice.getContent());
        assertThat(resultNotices.getWriter()).isEqualTo(userName);
        assertThat(resultNotices.getView()).isEqualTo(0);
        assertThat(resultNotices.getStartDate()).isEqualTo(saveNotice.getStartDate());
        assertThat(resultNotices.getEndDate()).isEqualTo(saveNotice.getEndDate());
        assertThat(resultAttachment.getOriginalName()).isEqualTo("newFile.txt");
        assertThat(resultAttachment.getSaveName()).contains("newFile");
        assertThat(resultAttachment.getExtension()).isEqualTo("text/plain");
    }

    @Test
    @DisplayName("공지사항 삭제 성공")
    void testDeleteNotice() {
        // given
        String title = "test title";
        String userName = "testUser";
        Notice saveNotice = Notice.builder().title(title).content("test content").startDate(LocalDateTime.now()).endDate(LocalDateTime.now().plusDays(1)).writer(userName).build();
        Notice notice = noticeRepository.store(saveNotice);
        Attachment saveAttachment = Attachment.builder().originalName("oldFile.txt").saveName("newFile.txt").extension("txt").size("12B").filePath("C:/rsupport/newFIle.txt").notice(notice).build();
        attachmentRepository.store(saveAttachment);
    }

}
