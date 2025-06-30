package com.project.admin.controller.classification;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.system.domain.Classification;
import com.project.system.service.ClassificationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 分类controller
 * @date 2023/11/17 02:15
 */
@Controller
@ResponseBody
@RequestMapping("classification")
public class ClassificationController {

    @Autowired
    private ClassificationService classificationService;

    /** 分页获取分类 */
    @Log(name = "分页获取分类", type = BusinessType.OTHER)
    @PostMapping("getApeClassificationPage")
    public Result getApeClassificationPage(@RequestBody Classification classification) {
        Page<Classification> page = new Page<>(classification.getPageNumber(), classification.getPageSize());
        QueryWrapper<Classification> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(classification.getName()), Classification::getName, classification.getName());
        Page<Classification> apeClassificationPage = classificationService.page(page, queryWrapper);
        return Result.success(apeClassificationPage);
    }

    /** 获取分类列表 */
    @Log(name = "获取分类列表", type = BusinessType.OTHER)
    @GetMapping("getApeClassificationList")
    public Result getApeClassificationList() {
        List<Classification> list = classificationService.list();
        return Result.success(list);
    }

    /** 根据id获取分类 */
    @Log(name = "根据id获取分类", type = BusinessType.OTHER)
    @GetMapping("getApeClassificationById")
    public Result getApeClassificationById(@RequestParam("id")String id) {
        Classification classification = classificationService.getById(id);
        return Result.success(classification);
    }

    /** 保存分类 */
    @Log(name = "保存分类", type = BusinessType.INSERT)
    @PostMapping("saveApeClassification")
    public Result saveApeClassification(@RequestBody Classification classification) {
        boolean save = classificationService.save(classification);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑分类 */
    @Log(name = "编辑分类", type = BusinessType.UPDATE)
    @PostMapping("editApeClassification")
    public Result editApeClassification(@RequestBody Classification classification) {
        boolean save = classificationService.updateById(classification);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除分类 */
    @GetMapping("removeApeClassification")
    @Log(name = "删除分类", type = BusinessType.DELETE)
    public Result removeApeClassification(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                classificationService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("分类id不能为空！");
        }
    }

}