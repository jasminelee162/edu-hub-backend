package com.project.system.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.project.common.enums.SharePermission;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@TableName("user_document")
public class UserDocument {
    @Id
    private String id;
    private String title;

    private byte[] content; // 可编辑内容

    @TableField("share_token")
    private String shareToken; // 分享标识

    @TableField("base_template")
    private Template baseTemplate;

    @TableField("user_id")
    private String userId;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("last_modified")
    private LocalDateTime lastModified;
    @TableField("share_permission")
    private SharePermission sharePermission = SharePermission.EDIT;
}
