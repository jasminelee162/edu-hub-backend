package com.project.admin.controller.homework;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.framework.utils.ShiroUtils;
import com.project.system.domain.Chapter;
import com.project.system.domain.Homework;
import com.project.system.domain.HomeworkStudent;
import com.project.system.domain.User;
import com.project.system.service.ChapterService;
import com.project.system.service.HomeworkService;
import com.project.system.service.HomeworkStudentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 学生作业controller
 * @date 2023/11/22 04:28
 */
@Controller
@ResponseBody
@RequestMapping("student")
public class HomeworkStudentController {

    @Autowired
    private HomeworkStudentService homeworkStudentService;
    @Autowired
    private HomeworkService homeworkService;
    @Autowired
    private ChapterService chapterService;

    /** 分页获取学生作业 */
    @Log(name = "分页获取学生作业", type = BusinessType.OTHER)
    @PostMapping("getApeHomeworkStudentPage")
    public Result getApeHomeworkStudentPage(@RequestBody HomeworkStudent homeworkStudent) {
        Page<HomeworkStudent> page = new Page<>(homeworkStudent.getPageNumber(), homeworkStudent.getPageSize());
        QueryWrapper<HomeworkStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(homeworkStudent.getChapterId()), HomeworkStudent::getChapterId, homeworkStudent.getChapterId())
                .eq(StringUtils.isNotBlank(homeworkStudent.getWorkId()), HomeworkStudent::getWorkId, homeworkStudent.getWorkId())
                .eq(StringUtils.isNotBlank(homeworkStudent.getTitle()), HomeworkStudent::getTitle, homeworkStudent.getTitle())
                .eq(StringUtils.isNotBlank(homeworkStudent.getUserId()), HomeworkStudent::getUserId, homeworkStudent.getUserId());
        Page<HomeworkStudent> apeHomeworkStudentPage = homeworkStudentService.page(page, queryWrapper);
        return Result.success(apeHomeworkStudentPage);
    }

    @GetMapping("getApeHomeworkStudentFlag")
    public Result getApeHomeworkStudentFlag(@RequestParam("id") String id) {
        User userInfo = ShiroUtils.getUserInfo();
        QueryWrapper<HomeworkStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(HomeworkStudent::getChapterId,id)
                .eq(HomeworkStudent::getUserId,userInfo.getId());
        int count = homeworkStudentService.count(queryWrapper);
        if (count > 0) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @GetMapping("getHomeworkStudentFlag")
    public Result getHomeworkStudentFlag(@RequestParam("id") String id,@RequestParam("userId")String userId) {
        User userInfo = ShiroUtils.getUserInfo();
        QueryWrapper<HomeworkStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(HomeworkStudent::getChapterId,id)
                .eq(HomeworkStudent::getUserId,userId);
        int count = homeworkStudentService.count(queryWrapper);
        if (count > 0) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @GetMapping("getMyApeHomework")
    public Result getMyApeHomework() {
        User userInfo = ShiroUtils.getUserInfo();
        QueryWrapper<HomeworkStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(HomeworkStudent::getUserId,userInfo.getId())
                .groupBy(HomeworkStudent::getChapterId);
        List<HomeworkStudent> studentList = homeworkStudentService.list(queryWrapper);
        for (HomeworkStudent student : studentList) {
            Chapter chapter = chapterService.getById(student.getChapterId());
            student.setTaskName(chapter.getTaskName());
        }
        return Result.success(studentList);
    }

    @PostMapping("getApeHomeworkStudentList")
    public Result getApeHomeworkStudentList(@RequestBody HomeworkStudent apeHomeworkStudent) {
        User userInfo = ShiroUtils.getUserInfo();
        QueryWrapper<Homework> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Homework::getChapterId,apeHomeworkStudent.getChapterId()).orderByAsc(Homework::getSort);
        List<Homework> homeworkList = homeworkService.list(queryWrapper);
        List<HomeworkStudent> homeworkStudentList = new ArrayList<>();
        for (Homework homework : homeworkList) {
            QueryWrapper<HomeworkStudent> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(HomeworkStudent::getUserId,userInfo.getId())
                            .eq(HomeworkStudent::getWorkId,homework.getId());
            HomeworkStudent homeworkStudent = homeworkStudentService.getOne(wrapper);
            if (homeworkStudent == null) {
                homeworkStudent = new HomeworkStudent();
                homeworkStudent.setChapterId(homework.getChapterId());
                homeworkStudent.setChapterName(homework.getChapterName());
                homeworkStudent.setWorkId(homework.getId());
                homeworkStudent.setTitle(homework.getTitle());
                homeworkStudent.setSort(homework.getSort());
                homeworkStudent.setAnswer(homework.getAnswer());
                homeworkStudent.setType(homework.getType());
                homeworkStudent.setScore(homework.getScore());
                homeworkStudent.setContent(homework.getContent());
                homeworkStudent.setUserId(userInfo.getId());
                homeworkStudent.setSolution("");
                homeworkStudent.setPoint(0);
            }
            homeworkStudentList.add(homeworkStudent);
        }
        return Result.success(homeworkStudentList);
    }



    /** 根据id获取学生作业 */
    @Log(name = "根据id获取学生作业", type = BusinessType.OTHER)
    @GetMapping("getApeHomeworkStudentById")
    public Result getApeHomeworkStudentById(@RequestParam("id")String id) {
        HomeworkStudent homeworkStudent = homeworkStudentService.getById(id);
        return Result.success(homeworkStudent);
    }

    /** 保存学生作业 */
    @Log(name = "保存学生作业", type = BusinessType.INSERT)
    @PostMapping("saveApeHomeworkStudent")
    public Result saveApeHomeworkStudent(@RequestBody JSONObject jsonObject) {
        JSONArray homework = jsonObject.getJSONArray("homework");
        List<HomeworkStudent> list = new ArrayList<>();
        for (int i = 0; i < homework.size(); i++) {
            HomeworkStudent homeworkStudent = homework.getJSONObject(i).toJavaObject(HomeworkStudent.class);
            if (StringUtils.isNotBlank(homeworkStudent.getSolution())) {
                if (homeworkStudent.getAnswer().equals(homeworkStudent.getSolution())) {
                    homeworkStudent.setPoint(homeworkStudent.getScore());
                }
            }
            list.add(homeworkStudent);
        }
        boolean save = homeworkStudentService.saveBatch(list);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑学生作业 */
    @Log(name = "编辑学生作业", type = BusinessType.UPDATE)
    @PostMapping("editApeHomeworkStudent")
    public Result editApeHomeworkStudent(@RequestBody HomeworkStudent homeworkStudent) {
        boolean save = homeworkStudentService.updateById(homeworkStudent);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除学生作业 */
    @GetMapping("removeApeHomeworkStudent")
    @Log(name = "删除学生作业", type = BusinessType.DELETE)
    public Result removeApeHomeworkStudent(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                homeworkStudentService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("学生作业id不能为空！");
        }
    }

    @GetMapping("getWrongWork")
    public Result getWrongWork(@RequestParam("id") String id) {
        User userInfo = ShiroUtils.getUserInfo();
        QueryWrapper<HomeworkStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(HomeworkStudent::getChapterId,id)
                .eq(HomeworkStudent::getUserId,userInfo.getId());
        List<HomeworkStudent> studentList = homeworkStudentService.list(queryWrapper);
        studentList = studentList.stream().filter(item -> {
            return !item.getAnswer().equals(item.getSolution());
        }).collect(Collectors.toList());
        return Result.success(studentList);
    }

}