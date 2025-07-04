package com.project.system.vo;

import lombok.Data;

@Data
public class DocumentVersionVO {
    private String documentId;
    private String changeVersion;
    private String changeNote;
}
