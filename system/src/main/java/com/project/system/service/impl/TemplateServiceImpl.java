package com.project.system.service.impl;


import com.project.common.domain.Result;
import com.project.system.domain.Template;
import com.project.system.mapper.TemplateMapper;
import com.project.system.service.TemplateService;
import com.project.system.vo.TemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateMapper templateMapper;

    //上传模板
    public Result uploadTemplate(MultipartFile file) {
        Template template = new Template();
        try {
            template.setName(file.getOriginalFilename());
            template.setFileType(file.getOriginalFilename());
            template.setFileContent(file.getBytes());
            template.setCreatedAt(LocalDateTime.now());
            templateMapper.insert(template);
            return Result.success();
        } catch (IOException e) {
            return Result.fail("模板上传失败");
        }
    }

    //展示所有模板VO
    public List<TemplateVO> getAllTemplates() {
        List<Template> templateList=templateMapper.selectList(null);
        return templateToVO(templateList);
    }

    private List<TemplateVO> templateToVO(List<Template> templateList) {
        List<TemplateVO> templateVOList=new ArrayList<>();
        for (Template template : templateList) {
            TemplateVO templateVO=new TemplateVO();
            templateVO.setId(templateVO.getId());
            templateVO.setName(template.getName());
            templateVO.setCreateAt(template.getCreatedAt());
            templateVOList.add(templateVO);
        }
        return templateVOList;
    }

    //展示文档内容
    public byte[] getTemplateById(String id) {
        return templateMapper.selectById(id).getFileContent();
    }


}
