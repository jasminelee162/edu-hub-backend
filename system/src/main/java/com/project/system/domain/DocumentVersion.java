package com.project.system.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@TableName("document_version")
public class DocumentVersion {
    @Id
    private String id;

    private byte[] content;

    @TableField("document_id")
    private String documentId;

    @TableField("created_by")
    private String createdBy;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("change_note")
    private String changeNote;

    @TableField("change_version")
    private String changeVersion;
}
