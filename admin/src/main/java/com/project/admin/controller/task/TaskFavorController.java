package com.project.admin.controller.task;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.framework.utils.ShiroUtils;
import com.project.system.domain.Task;
import com.project.system.domain.TaskFavor;
import com.project.system.domain.User;
import com.project.system.service.TaskFavorService;
import com.project.system.service.TaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 课程收藏controller
 * @date 2024/01/18 01:51
 */
@Controller
@ResponseBody
@RequestMapping("favor")
public class TaskFavorController {

    @Autowired
    private TaskFavorService taskFavorService;
    @Autowired
    private TaskService taskService;

    /** 分页获取课程收藏 */
    @Log(name = "分页获取课程收藏", type = BusinessType.OTHER)
    @PostMapping("getApeTaskFavorPage")
    public Result getApeTaskFavorPage(@RequestBody TaskFavor taskFavor) {
        User user = ShiroUtils.getUserInfo();
        taskFavor.setUserId(user.getId());
        Page<TaskFavor> page = new Page<>(taskFavor.getPageNumber(), taskFavor.getPageSize());
        QueryWrapper<TaskFavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(taskFavor.getTaskId()), TaskFavor::getTaskId, taskFavor.getTaskId())
                .eq(StringUtils.isNotBlank(taskFavor.getUserId()), TaskFavor::getUserId, taskFavor.getUserId())
                .eq(StringUtils.isNotBlank(taskFavor.getTaskName()), TaskFavor::getTaskName, taskFavor.getTaskName())
                .eq(StringUtils.isNotBlank(taskFavor.getTaskImage()), TaskFavor::getTaskImage, taskFavor.getTaskImage())
                .eq(StringUtils.isNotBlank(taskFavor.getTaskDesc()), TaskFavor::getTaskDesc, taskFavor.getTaskDesc());
        Page<TaskFavor> apeTaskFavorPage = taskFavorService.page(page, queryWrapper);
        return Result.success(apeTaskFavorPage);
    }



    /** 根据id获取课程收藏 */
    @Log(name = "根据id获取课程收藏", type = BusinessType.OTHER)
    @GetMapping("getApeTaskFavorById")
    public Result getApeTaskFavorById(@RequestParam("taskId")String taskId,@RequestParam("userId")String userId) {
        QueryWrapper<TaskFavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TaskFavor::getTaskId,taskId).eq(TaskFavor::getUserId,userId).last("limit 1");
        TaskFavor taskFavor = taskFavorService.getOne(queryWrapper);
        if (taskFavor != null) {
            return Result.success(taskFavor);
        } else {
            return Result.fail();
        }
    }

    /** 保存课程收藏 */
    @Log(name = "保存课程收藏", type = BusinessType.INSERT)
    @PostMapping("saveApeTaskFavor")
    public Result saveApeTaskFavor(@RequestBody TaskFavor taskFavor) {
        Task task = taskService.getById(taskFavor.getTaskId());
        taskFavor.setTaskDesc(task.getTaskDescribe());
        taskFavor.setTaskName(task.getName());
        taskFavor.setTaskImage(task.getImage());
        boolean save = taskFavorService.save(taskFavor);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑课程收藏 */
    @Log(name = "编辑课程收藏", type = BusinessType.UPDATE)
    @PostMapping("editApeTaskFavor")
    public Result editApeTaskFavor(@RequestBody TaskFavor taskFavor) {
        boolean save = taskFavorService.updateById(taskFavor);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除课程收藏 */
    @GetMapping("removeApeTaskFavor")
    @Log(name = "删除课程收藏", type = BusinessType.DELETE)
    public Result removeApeTaskFavor(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                taskFavorService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("课程收藏id不能为空！");
        }
    }

}