package com.project.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    // 分页查询，支持名称和时间范围筛选
    @Override
    public Result queryTemplates(String name, Integer pageNumber, Integer pageSize, String startDate, String endDate) {
        if (pageNumber == null || pageNumber <= 0) pageNumber = 1;
        if (pageSize == null || pageSize <= 0) pageSize = 10;

        Page<Template> page = new Page<>(pageNumber, pageSize);
        LambdaQueryWrapper<Template> wrapper = new LambdaQueryWrapper<>();

        if (name != null && !name.trim().isEmpty()) {
            wrapper.like(Template::getName, name);
        }
        if (startDate != null && !startDate.isEmpty()) {
            wrapper.ge(Template::getCreatedAt, startDate + " 00:00:00");
        }
        if (endDate != null && !endDate.isEmpty()) {
            wrapper.le(Template::getCreatedAt, endDate + " 23:59:59");
        }

        IPage<Template> resultPage = templateMapper.selectPage(page, wrapper);

        // 转成VO返回
        List<TemplateVO> voList = templateToVO(resultPage.getRecords());

        return Result.success(new java.util.HashMap<String, Object>() {{
            put("records", voList);
            put("total", resultPage.getTotal());
        }});
    }

    //删除资料
    @Override
    public Result deleteTemplatesByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.fail("未提供要删除的ID");
        }
        int deleted = templateMapper.deleteBatchIds(ids);
        return Result.success("成功删除 " + deleted + " 条记录");
    }
    //上传模板
    public Result uploadTemplate(MultipartFile file) {
        Template template = new Template();
        try {
            template.setId(templateMapper.selectList(null).size());
            template.setName(file.getOriginalFilename());
            int lastDotIndex = file.getOriginalFilename().lastIndexOf('.');
            String extension="";
            if (lastDotIndex != -1 && lastDotIndex < file.getOriginalFilename().length() - 1) {
                extension= file.getOriginalFilename().substring(lastDotIndex + 1);
            }
            template.setFileType(extension);
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
            templateVO.setId(template.getId());
            templateVO.setName(template.getName());
            templateVO.setCreateAt(template.getCreatedAt());
            templateVOList.add(templateVO);
        }
        return templateVOList;
    }

    //展示文档内容
    public Template getTemplateById(String id) {
        return templateMapper.selectById(id);
    }


}