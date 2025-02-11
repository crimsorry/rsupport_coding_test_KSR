package com.rsupport.notice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachmentId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String saveName;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private String extension;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted;

    @PrePersist
    public void prePersist() {
        if (this.isDeleted == null) this.isDeleted = Boolean.FALSE;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public void softDelete(){
        this.isDeleted = true;
    }

}