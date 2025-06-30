package com.project.admin.controller.major;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.system.domain.Major;
import com.project.system.service.MajorService;
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
 * @description: 专业表controller
 * @date 2023/11/16 08:31
 */
@Controller
@ResponseBody
@RequestMapping("major")
public class MajorController {

    @Autowired
    private MajorService majorService;

    /** 分页获取专业表 */
    @Log(name = "分页获取专业表", type = BusinessType.OTHER)
    @PostMapping("getApeMajorPage")
    public Result getApeMajorPage(@RequestBody Major major) {
        Page<Major> page = new Page<>(major.getPageNumber(), major.getPageSize());
        QueryWrapper<Major> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(major.getName()), Major::getName, major.getName());
        Page<Major> apeMajorPage = majorService.page(page, queryWrapper);
        return Result.success(apeMajorPage);
    }

    /** 获取专业列表 */
    @Log(name = "获取专业列表", type = BusinessType.OTHER)
    @GetMapping("getApeMajorList")
    public Result getApeMajorList() {
        List<Major> majorList = majorService.list();
        return Result.success(majorList);
    }

    /** 根据id获取专业表 */
    @Log(name = "根据id获取专业表", type = BusinessType.OTHER)
    @GetMapping("getApeMajorById")
    public Result getApeMajorById(@RequestParam("id")String id) {
        Major major = majorService.getById(id);
        return Result.success(major);
    }

    /** 保存专业表 */
    @Log(name = "保存专业表", type = BusinessType.INSERT)
    @PostMapping("saveApeMajor")
    public Result saveApeMajor(@RequestBody Major major) {
        boolean save = majorService.save(major);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑专业表 */
    @Log(name = "编辑专业表", type = BusinessType.UPDATE)
    @PostMapping("editApeMajor")
    public Result editApeMajor(@RequestBody Major major) {
        boolean save = majorService.updateById(major);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除专业表 */
    @GetMapping("removeApeMajor")
    @Log(name = "删除专业表", type = BusinessType.DELETE)
    public Result removeApeMajor(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                majorService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("专业表id不能为空！");
        }
    }

}