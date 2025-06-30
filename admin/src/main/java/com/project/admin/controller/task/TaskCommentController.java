package com.project.admin.controller.task;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.framework.utils.ShiroUtils;
import com.project.system.domain.TaskComment;
import com.project.system.domain.User;
import com.project.system.service.TaskCommentService;
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
 * @description: 课程评论controller
 * @date 2023/11/21 08:12
 */
@Controller
@ResponseBody
@RequestMapping("comment")
public class TaskCommentController {

    @Autowired
    private TaskCommentService taskCommentService;

    /** 分页获取课程评论 */
    @Log(name = "分页获取课程评论", type = BusinessType.OTHER)
    @PostMapping("getApeTaskCommentPage")
    public Result getApeTaskCommentPage(@RequestBody TaskComment taskComment) {
        Page<TaskComment> page = new Page<>(taskComment.getPageNumber(), taskComment.getPageSize());
        QueryWrapper<TaskComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(taskComment.getTaskId()), TaskComment::getTaskId, taskComment.getTaskId())
                .like(StringUtils.isNotBlank(taskComment.getContent()), TaskComment::getContent, taskComment.getContent())
                .like(StringUtils.isNotBlank(taskComment.getCreateBy()), TaskComment::getCreateBy, taskComment.getCreateBy());
        Page<TaskComment> apeTaskCommentPage = taskCommentService.page(page, queryWrapper);
        return Result.success(apeTaskCommentPage);
    }

    /** 根据id获取课程评论 */
    @Log(name = "根据id获取课程评论", type = BusinessType.OTHER)
    @GetMapping("getApeTaskCommentById")
    public Result getApeTaskCommentById(@RequestParam("id")String id) {
        TaskComment taskComment = taskCommentService.getById(id);
        return Result.success(taskComment);
    }

    @GetMapping("getApeTaskCommentListByTaskId")
    public Result getApeTaskCommentListByTaskId(@RequestParam("id")String id) {
        QueryWrapper<TaskComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TaskComment::getTaskId,id).orderByDesc(TaskComment::getCreateTime);
        List<TaskComment> commentList = taskCommentService.list(queryWrapper);
        return Result.success(commentList);
    }

    /** 保存课程评论 */
    @Log(name = "保存课程评论", type = BusinessType.INSERT)
    @PostMapping("saveApeTaskComment")
    public Result saveApeTaskComment(@RequestBody TaskComment taskComment) {
        User userInfo = ShiroUtils.getUserInfo();
        taskComment.setAvatar(userInfo.getAvatar());
        boolean save = taskCommentService.save(taskComment);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑课程评论 */
    @Log(name = "编辑课程评论", type = BusinessType.UPDATE)
    @PostMapping("editApeTaskComment")
    public Result editApeTaskComment(@RequestBody TaskComment taskComment) {
        User userInfo = ShiroUtils.getUserInfo();
        taskComment.setAvatar(userInfo.getAvatar());
        boolean save = taskCommentService.updateById(taskComment);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除课程评论 */
    @GetMapping("removeApeTaskComment")
    @Log(name = "删除课程评论", type = BusinessType.DELETE)
    public Result removeApeTaskComment(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                taskCommentService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("课程评论id不能为空！");
        }
    }

}