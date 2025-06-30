package com.project.admin.controller.school;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.system.domain.School;
import com.project.system.service.SchoolService;
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
 * @description: 学校表controller
 * @date 2023/11/16 08:03
 */
@Controller
@ResponseBody
@RequestMapping("school")
public class SchoolController {

    @Autowired
    private SchoolService schoolService;

    /** 分页获取学校表 */
    @Log(name = "分页获取学校表", type = BusinessType.OTHER)
    @PostMapping("getApeSchoolPage")
    public Result getApeSchoolPage(@RequestBody School school) {
        Page<School> page = new Page<>(school.getPageNumber(), school.getPageSize());
        QueryWrapper<School> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(school.getName()), School::getName, school.getName());
        Page<School> apeSchoolPage = schoolService.page(page, queryWrapper);
        return Result.success(apeSchoolPage);
    }

    /** 获取学校列表 */
    @Log(name = "获取学校列表", type = BusinessType.OTHER)
    @GetMapping("getApeSchoolList")
    public Result getApeSchoolList() {
        List<School> schoolList = schoolService.list();
        return Result.success(schoolList);
    }

    /** 根据id获取学校表 */
    @Log(name = "根据id获取学校表", type = BusinessType.OTHER)
    @GetMapping("getApeSchoolById")
    public Result getApeSchoolById(@RequestParam("id")String id) {
        School school = schoolService.getById(id);
        return Result.success(school);
    }

    /** 保存学校表 */
    @Log(name = "保存学校表", type = BusinessType.INSERT)
    @PostMapping("saveApeSchool")
    public Result saveApeSchool(@RequestBody School school) {
        boolean save = schoolService.save(school);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑学校表 */
    @Log(name = "编辑学校表", type = BusinessType.UPDATE)
    @PostMapping("editApeSchool")
    public Result editApeSchool(@RequestBody School school) {
        boolean save = schoolService.updateById(school);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除学校表 */
    @GetMapping("removeApeSchool")
    @Log(name = "删除学校表", type = BusinessType.DELETE)
    public Result removeApeSchool(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                schoolService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("学校表id不能为空！");
        }
    }

}