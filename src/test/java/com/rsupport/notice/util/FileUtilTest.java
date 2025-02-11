package com.rsupport.notice.util;

import com.rsupport.notice.domain.entity.Attachment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FileUtilTest {@Autowired
private FileUtil fileUtil;

    @Test
    @DisplayName("파일 업로드 성공")
    void testUploadFileSuccess() {
        // given
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.txt", "text/plain", "RSupport Upload Test".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.txt", "text/plain", "RSupport Upload Test2".getBytes());
        List<MultipartFile> fileList = List.of(file1, file2);

        // when
        List<Attachment> result = fileUtil.uploadFile(fileList);

        // then
        assertNotNull(result);
        assertThat(result.size()).isEqualTo(2);

        for (Attachment attachment : result) {
            File uploadedFile = new File(attachment.getFilePath());
            assertTrue(uploadedFile.exists(), "업로드된 파일이 존재해야 합니다.");
        }
    }

}
