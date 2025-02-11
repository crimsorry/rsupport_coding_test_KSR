package com.rsupport.notice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class NoticeRequestDto {

    @NotBlank
    @Size(min=2, max=100)
    private String title;

    @NotBlank
    @Size(min=2, max=10000)
    private String content;

    @Size(min=1, max=50)
    private String modifiedWriter;

    @Size(min=1, max=50)
    private String deleteWriter;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

}
