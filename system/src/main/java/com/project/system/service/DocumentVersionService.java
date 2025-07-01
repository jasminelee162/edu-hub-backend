package com.project.system.service;


import com.project.system.domain.UserDocument;
import com.project.system.vo.DocumentVersionVO;

import java.util.List;

public interface DocumentVersionService {
    void saveVersion(UserDocument doc, String id, String changeNote);
    List<DocumentVersionVO> getAllVersion(String documentId);
    byte[] getVersionById(String id);
}
