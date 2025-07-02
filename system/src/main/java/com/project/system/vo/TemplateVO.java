package com.project.system.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TemplateVO {
    private String id;
    private String name;
    private String filetype;
    private LocalDateTime createAt;
}
