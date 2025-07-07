package com.project.system.service;



import com.project.common.domain.Result;
import com.project.system.domain.Template;
import com.project.system.vo.TemplateVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TemplateService {
    Result uploadTemplate(MultipartFile file);
    List<TemplateVO> getAllTemplates();
    Template getTemplateById(String id);
    // 新增分页查询方法
    Result queryTemplates(String name, Integer pageNumber, Integer pageSize, String startDate, String endDate);
    //删除资料
    Result deleteTemplatesByIds(List<Integer> ids);
}