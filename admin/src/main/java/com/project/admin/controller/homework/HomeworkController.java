package com.project.admin.controller.homework;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.system.domain.Chapter;
import com.project.system.domain.Homework;
import com.project.system.service.ChapterService;
import com.project.system.service.HomeworkService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 作业controller
 * @date 2023/11/18 09:06
 */
@Controller
@ResponseBody
@RequestMapping("homework")
public class HomeworkController {

    @Autowired
    private HomeworkService homeworkService;
    @Autowired
    private ChapterService chapterService;

    /** 分页获取作业 */
    @Log(name = "分页获取作业", type = BusinessType.OTHER)
    @PostMapping("getHomeworkPage")
    public Result getHomeworkPage(@RequestBody Homework homework) {
        Page<Homework> page = new Page<>(homework.getPageNumber(), homework.getPageSize());
        QueryWrapper<Homework> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(homework.getTitle()), Homework::getTitle, homework.getTitle())
                .eq(homework.getType() != null, Homework::getType, homework.getType())
                .orderByAsc(Homework::getSort);
        Page<Homework> HomeworkPage = homeworkService.page(page, queryWrapper);
        return Result.success(HomeworkPage);
    }

    /** 根据id获取作业 */
    @Log(name = "根据id获取作业", type = BusinessType.OTHER)
    @GetMapping("getHomeworkById")
    public Result getHomeworkById(@RequestParam("id")String id) {
        Homework homework = homeworkService.getById(id);
        return Result.success(homework);
    }

    @GetMapping("getHomeworkByChapterId")
    public Result getHomeworkByChapterId(@RequestParam("id")String id) {
        QueryWrapper<Homework> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Homework::getChapterId,id);
        int count = homeworkService.count(queryWrapper);
        if (count > 0) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    /** 保存作业 */
    @Log(name = "保存作业", type = BusinessType.INSERT)
    @PostMapping("saveHomework")
    public Result saveHomework(@RequestBody Homework homework) {
        if (StringUtils.isNotBlank(homework.getChapterId())) {
            Chapter chapter = chapterService.getById(homework.getChapterId());
            homework.setChapterName(chapter.getName());
        }
        boolean save = homeworkService.save(homework);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑作业 */
    @Log(name = "编辑作业", type = BusinessType.UPDATE)
    @PostMapping("editHomework")
    public Result editHomework(@RequestBody Homework homework) {
        if (StringUtils.isNotBlank(homework.getChapterId())) {
            Chapter chapter = chapterService.getById(homework.getChapterId());
            homework.setChapterName(chapter.getName());
        }
        boolean save = homeworkService.updateById(homework);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除作业 */
    @GetMapping("removeHomework")
    @Log(name = "删除作业", type = BusinessType.DELETE)
    public Result removeHomework(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                homeworkService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("作业id不能为空！");
        }
    }

}