package com.rsupport.notice.domain.entity;

import com.rsupport.notice.core.ErrorCode;
import com.rsupport.notice.support.error.FailException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String writer;

    @Column
    private String modifiedWriter;

    @Column
    private String deleteWriter;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long view;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted;

    @PrePersist
    public void prePersist() {
        if (this.view == null) this.view = 0L;
        if (this.isDeleted == null) this.isDeleted = false;
    }

    public void updateContent(String title, String content, LocalDateTime startDate, LocalDateTime endDate, String modifiedWriter) {
        if(title != null) this.title = title;
        if(content != null) this.content = content;
        if(startDate != null) this.startDate = startDate;
        if(endDate != null) this.endDate = endDate;
        if(modifiedWriter != null) this.modifiedWriter = modifiedWriter;
    }

    public void softDelete(String userName) {
        this.isDeleted = true;
        this.deleteWriter = userName;
    }

    public void validateOpenDate(){
        if(this.startDate.isAfter(this.endDate)) {
            throw new FailException(ErrorCode.NOTICE_DATE_ORDER_INVALID);
        }
    }

    public void increaseView(){
        this.view++;
    }

}
