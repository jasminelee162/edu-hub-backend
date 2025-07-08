package com.project.admin.controller.document;


import com.project.common.domain.Result;
import com.project.system.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    // 支持分页查询的展示
    @GetMapping("/show")
    public Result showTemplate(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        return templateService.queryTemplates(name, pageNumber, pageSize, startDate, endDate);
    }
    //删除资料
    @PostMapping("/delete")
    public Result deleteTemplates(@RequestBody List<Integer> ids) {
        return templateService.deleteTemplatesByIds(ids);
    }
    //展示文档内容
    @GetMapping("/content")
    public Result showTemplateContent(@RequestParam String id) {
        return Result.success(templateService.getTemplateById(id));
    }

}
