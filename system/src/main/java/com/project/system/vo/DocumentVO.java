package com.project.system.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.project.common.enums.SharePermission;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentVO {
    private String id;
    private String title;
    private byte[] content; // 可编辑内容
    private String shareToken; // 分享标识
    private String baseTemplateId;
    private String userId;
    private String userCollaboration;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
    private SharePermission sharePermission;
    private String fileType;
}
