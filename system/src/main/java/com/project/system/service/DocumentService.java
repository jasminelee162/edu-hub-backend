package com.project.system.service;

public interface DocumentService {
    String createFromTemplate(String templateId,String userId);
    void updateContent(String docId, byte[] content);
    byte[] getContent(String docId);
}
