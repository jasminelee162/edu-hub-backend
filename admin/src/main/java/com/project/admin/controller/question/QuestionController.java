package com.project.admin.controller.question;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.framework.utils.ShiroUtils;
import com.project.system.domain.Question;
import com.project.system.domain.Task;
import com.project.system.domain.User;
import com.project.system.service.QuestionService;
import com.project.system.service.TaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @version 1.0
 * @description: 答疑controller
 */
@Controller
@ResponseBody
@RequestMapping("question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private TaskService taskService;

    /** 分页获取答疑 */
    @Log(name = "分页获取答疑", type = BusinessType.OTHER)
    @PostMapping("getQuestionPage")
    public Result getQuestionPage(@RequestBody Question question) {
        Page<Question> page = new Page<>(question.getPageNumber(), question.getPageSize());
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(question.getUserName()), Question::getUserName, question.getUserName())
                .like(StringUtils.isNotBlank(question.getContent()), Question::getContent, question.getContent())
                .like(StringUtils.isNotBlank(question.getAnswer()), Question::getAnswer, question.getAnswer())
                .eq(StringUtils.isNotBlank(question.getTaskId()), Question::getTaskId, question.getTaskId())
                .eq(StringUtils.isNotBlank(question.getTeacherId()), Question::getTeacherId, question.getTeacherId())
                .like(StringUtils.isNotBlank(question.getTaskName()), Question::getTaskName, question.getTaskName());
        Page<Question> questionPage = questionService.page(page, queryWrapper);
        return Result.success(questionPage);
    }

    /** 根据id获取答疑 */
    @Log(name = "根据id获取答疑", type = BusinessType.OTHER)
    @GetMapping("getQuestionById")
    public Result getQuestionById(@RequestParam("id")String id) {
        Question question = questionService.getById(id);
        return Result.success(question);
    }

    /** 保存答疑 */
    @Log(name = "保存答疑", type = BusinessType.INSERT)
    @PostMapping("saveQuestion")
    public Result saveQuestion(@RequestBody Question question) {
        User user = ShiroUtils.getUserInfo();
        question.setUserId(user.getId());
        question.setUserName(user.getUserName());
        Task task = taskService.getById(question.getTaskId());
        question.setTaskId(task.getId());
        question.setTaskName(task.getName());
        question.setTeacherId(task.getTeacherId());
        boolean save = questionService.save(question);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑答疑 */
    @Log(name = "编辑答疑", type = BusinessType.UPDATE)
    @PostMapping("editQuestion")
    public Result editQuestion(@RequestBody Question question) {
        boolean save = questionService.updateById(question);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除答疑 */
    @GetMapping("removeQuestion")
    @Log(name = "删除答疑", type = BusinessType.DELETE)
    public Result removeQuestion(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                questionService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("答疑id不能为空！");
        }
    }

}