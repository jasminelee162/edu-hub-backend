package com.project.admin.controller.document;


import com.project.common.domain.Result;
import com.project.system.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@ResponseBody
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;
    //上传文件模板
    @PostMapping("/update")
    public Result addTemplate(MultipartFile file) {
        return templateService.uploadTemplate(file);
    }

    //展示所有模板VO，包括ID、name、创建时间
    @GetMapping("/show")
    public Result showTemplate() {
        return Result.success(templateService.getAllTemplates());
    }

    //展示文档内容
    @GetMapping("/content")
    public Result showTemplateContent(@RequestParam String id) {
        return Result.success(templateService.getTemplateById(id));
    }

}
