package com.project.system.service;

import java.util.List;

public interface DocumentService {
    String createFromTemplate(String templateId,String userId);
    void updateContent(String docId, byte[] content);
    byte[] getContent(String docId);
    List<String> joinCollaboration(String docId, String userId);
}
