package com.project.system.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.project.common.enums.SharePermission;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@TableName("document_template")
public class Template {
    @Id
    @TableId(type = IdType.AUTO)
    private int id;
    private String name;

    @TableField("file_type")
    private String fileType;

    @TableField("file_content")
    private byte[] fileContent;

    @TableField("html_content")
    private String htmlContent;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("share_permission")
    private SharePermission sharePermission = SharePermission.VIEW;

}
