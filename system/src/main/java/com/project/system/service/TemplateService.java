package com.project.system.service;



import com.project.common.domain.Result;
import com.project.system.vo.TemplateVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TemplateService {
    Result uploadTemplate(MultipartFile file);
    List<TemplateVO> getAllTemplates();
    byte[] getTemplateById(String id);
}
