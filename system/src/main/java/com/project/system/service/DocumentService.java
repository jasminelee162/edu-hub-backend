package com.project.system.service;

import com.project.common.domain.Result;

import java.util.List;

public interface DocumentService {
    String createFromTemplate(String templateId,String userId);
    void updateContent(String docId, byte[] content);
    byte[] getContent(String docId);
    String joinCollaboration(String docId, String userId);
    Result exitCollaboration(String docId, String userId);
}
