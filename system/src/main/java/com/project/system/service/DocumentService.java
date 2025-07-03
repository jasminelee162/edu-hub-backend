package com.project.system.service;

import java.util.List;

public interface DocumentService {
    String createFromTemplate(String templateId,String userId);
    void updateContent(String docId, byte[] content);
    byte[] getContent(String docId);
    void joinCollaboration(String docId, String userId);
    List<String> exitCollaboration(String docId, String userId);
}
