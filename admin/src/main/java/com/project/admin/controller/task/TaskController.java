package com.project.admin.controller.task;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.framework.utils.ShiroUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system.domain.*;
import com.project.system.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version 1.0
 * @description: 课程controller
 */
@Controller
@ResponseBody
@RequestMapping("task")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private TaskCommentService taskCommentService;
    @Autowired
    private TaskStudentService taskStudentService;
    @Autowired
    private TestService testService;
    @Autowired
    private TestStudentService testStudentService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleFavorService articleFavorService;
    @Autowired
    private ArticleCommentService articleCommentService;
    @Autowired
    private ChapterVideoService chapterVideoService;
    @Autowired
    private HomeworkService homeworkService;
    @Autowired
    private HomeworkStudentService homeworkStudentService;

    /** 分页获取课程 */
    @Log(name = "分页获取课程", type = BusinessType.OTHER)
    @PostMapping("getTaskPage")
    public Result getTaskPage(@RequestBody Task task) {
        if (task.getType() == 1) {
            task.setTeacherId(ShiroUtils.getUserInfo().getId());
        }
        Page<Task> page = new Page<>(task.getPageNumber(), task.getPageSize());
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(task.getName()), Task::getName, task.getName())
                .like(StringUtils.isNotBlank(task.getTaskDescribe()), Task::getTaskDescribe, task.getTaskDescribe())
                .like(StringUtils.isNotBlank(task.getTeacherName()), Task::getTeacherName, task.getTeacherName())
                .eq(StringUtils.isNotBlank(task.getTeacherId()), Task::getTeacherId, task.getTeacherId())
                .eq(task.getState() != null, Task::getState, task.getState())
                .like(StringUtils.isNotBlank(task.getMajor()), Task::getMajor, task.getMajor())
                .like(StringUtils.isNotBlank(task.getClassification()), Task::getClassification, task.getClassification());
        Page<Task> TaskPage = taskService.page(page, queryWrapper);
        return Result.success(TaskPage);
    }

    /** 根据id获取课程 */
    @Log(name = "根据id获取课程", type = BusinessType.OTHER)
    @GetMapping("getTaskById")
    public Result getTaskById(@RequestParam("id")String id) {
        Task task = taskService.getById(id);
        return Result.success(task);
    }

    @GetMapping("getTaskByTeacher")
    public Result getTaskByTeacher(@RequestParam("id")String id) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Task::getTeacherId,id);
        List<Task> taskList = taskService.list(queryWrapper);
        return Result.success(taskList);
    }

    @GetMapping("getTaskByTeacherId")
    public Result getTaskByTeacherId() {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Task::getTeacherId,ShiroUtils.getUserInfo().getId());
        List<Task> taskList = taskService.list(queryWrapper);
        return Result.success(taskList);
    }

    /** 获取课程列表 */
    @Log(name = "获取课程列表", type = BusinessType.OTHER)
    @GetMapping("getTaskList")
    public Result getTaskList() {
        List<Task> taskList = taskService.list();
        return Result.success(taskList);
    }

    /** 保存课程 */
    @Log(name = "保存课程", type = BusinessType.INSERT)
    @PostMapping("saveTask")
    public Result saveTask(@RequestBody Task task) {
        if (task.getType() == 1) {
            task.setTeacherId(ShiroUtils.getUserInfo().getId());
        }
        User user = userService.getById(task.getTeacherId());
        task.setTeacherName(user.getUserName());
        boolean save = taskService.save(task);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑课程 */
    @Log(name = "编辑课程", type = BusinessType.UPDATE)
    @PostMapping("editTask")
    public Result editTask(@RequestBody Task task) {
        if (StringUtils.isNotBlank(task.getTeacherId())) {
            User user = userService.getById(task.getTeacherId());
            task.setTeacherName(user.getUserName());
        }
        boolean save = taskService.updateById(task);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除课程 */
    @Transactional(rollbackFor = Exception.class)
    @GetMapping("removeTask")
    @Log(name = "删除课程", type = BusinessType.DELETE)
    public Result removeTask(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                taskService.removeById(id);
                QueryWrapper<TaskComment> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(TaskComment::getTaskId,id);
                taskCommentService.remove(queryWrapper);
                QueryWrapper<TaskStudent> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.lambda().eq(TaskStudent::getTaskId,id);
                taskStudentService.remove(queryWrapper1);
                QueryWrapper<Chapter> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.lambda().eq(Chapter::getTaskId,id);
                List<Chapter> chapterList = chapterService.list(queryWrapper2);
                for (Chapter chapter : chapterList) {
                    chapterService.removeById(chapter.getId());
                    QueryWrapper<ChapterVideo> queryWrapper3 = new QueryWrapper<>();
                    queryWrapper3.lambda().eq(ChapterVideo::getChapterId,chapter.getId());
                    chapterVideoService.remove(queryWrapper3);
                    QueryWrapper<Homework> queryWrapper4 = new QueryWrapper<>();
                    queryWrapper4.lambda().eq(Homework::getChapterId,chapter.getId());
                    homeworkService.remove(queryWrapper4);
                    QueryWrapper<HomeworkStudent> queryWrapper5 = new QueryWrapper<>();
                    queryWrapper5.lambda().eq(HomeworkStudent::getChapterId,chapter.getId());
                    homeworkStudentService.remove(queryWrapper5);
                }
                QueryWrapper<Article> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.lambda().eq(Article::getTaskId,id);
                List<Article> articleList = articleService.list(queryWrapper3);
                for (Article article : articleList) {
                    articleService.removeById(article.getId());
                    QueryWrapper<ArticleFavor> queryWrapper4 = new QueryWrapper<>();
                    queryWrapper4.lambda().eq(ArticleFavor::getArticleId, article.getId());
                    articleFavorService.remove(queryWrapper4);
                    QueryWrapper<ArticleComment> queryWrapper5 = new QueryWrapper<>();
                    queryWrapper5.lambda().eq(ArticleComment::getTaskId, article.getId());
                    articleCommentService.remove(queryWrapper5);
                }
                QueryWrapper<Test> queryWrapper5 = new QueryWrapper<>();
                queryWrapper5.lambda().eq(Test::getTaskId,id);
                List<Test> testList = testService.list(queryWrapper5);
                for (Test test : testList) {
                    testService.removeById(test.getId());
                    QueryWrapper<TestStudent> queryWrapper6 = new QueryWrapper<>();
                    queryWrapper6.lambda().eq(TestStudent::getTestId, test.getId());
                    testStudentService.remove(queryWrapper6);
                }
            }
            return Result.success();
        } else {
            return Result.fail("课程id不能为空！");
        }
    }

}