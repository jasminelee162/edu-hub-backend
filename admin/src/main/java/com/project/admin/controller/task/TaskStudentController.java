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
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 课程报名controller
 * @date 2023/11/21 03:15
 */
@Controller
@ResponseBody
@RequestMapping("student")
public class TaskStudentController {

    @Autowired
    private TaskStudentService taskStudentService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HomeworkStudentService homeworkStudentService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private ChapterVideoService chapterVideoService;
    @Autowired
    private TestStudentService testStudentService;
    @Autowired
    private TestService testService;
    @Autowired
    private HomeworkService homeworkService;

    /** 分页获取课程报名 */
    @Log(name = "分页获取课程报名", type = BusinessType.OTHER)
    @PostMapping("getApeTaskStudentPage")
    public Result getApeTaskStudentPage(@RequestBody TaskStudent taskStudent) {
        Page<TaskStudent> page = new Page<>(taskStudent.getPageNumber(), taskStudent.getPageSize());
        QueryWrapper<TaskStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(taskStudent.getTaskId()), TaskStudent::getTaskId, taskStudent.getTaskId())
                .like(StringUtils.isNotBlank(taskStudent.getUserName()), TaskStudent::getUserName, taskStudent.getUserName())
                .eq(taskStudent.getState() != null, TaskStudent::getState, taskStudent.getState());
        Page<TaskStudent> apeTaskStudentPage = taskStudentService.page(page, queryWrapper);
        return Result.success(apeTaskStudentPage);
    }

    @PostMapping("getTaskStudentPage")
    public Result getTaskStudentPage(@RequestBody TaskStudent taskStudent) {
        Chapter chapter = chapterService.getById(taskStudent.getChapterId());
        Page<TaskStudent> page = new Page<>(taskStudent.getPageNumber(), taskStudent.getPageSize());
        QueryWrapper<TaskStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(taskStudent.getTaskId()), TaskStudent::getTaskId, taskStudent.getTaskId())
                .like(StringUtils.isNotBlank(taskStudent.getUserName()), TaskStudent::getUserName, taskStudent.getUserName())
                .eq(taskStudent.getState() != null, TaskStudent::getState, taskStudent.getState());
        Page<TaskStudent> taskStudentPage = taskStudentService.page(page, queryWrapper);
        for (TaskStudent record : taskStudentPage.getRecords()) {
            QueryWrapper<HomeworkStudent> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(HomeworkStudent::getChapterId, chapter.getId())
                    .eq(HomeworkStudent::getUserId, record.getUserId());
            int count = homeworkStudentService.count(wrapper);
            record.setHomework(count > 0 ? "已完成" : "未完成");

            QueryWrapper<ChapterVideo> videoQueryWrapper = new QueryWrapper<>();
            videoQueryWrapper.lambda().eq(ChapterVideo::getChapterId, chapter.getId())
                    .eq(ChapterVideo::getUserId, record.getUserId());
            int count1 = chapterVideoService.count(videoQueryWrapper);
            record.setVideo(count1 > 0 ? "已观看" : "未观看");
        }
        return Result.success(taskStudentPage);
    }

    /** 分页获取课程报名 */
    @Log(name = "分页获取我的课程", type = BusinessType.OTHER)
    @PostMapping("getMyTaskPage")
    public Result getMyTaskPage(@RequestBody TaskStudent taskStudent) {
        User userInfo = ShiroUtils.getUserInfo();
        taskStudent.setUserId(userInfo.getId());
        Page<TaskStudent> page = new Page<>(taskStudent.getPageNumber(), taskStudent.getPageSize());
        QueryWrapper<TaskStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(taskStudent.getUserId()), TaskStudent::getUserId, taskStudent.getUserId())
                .eq(taskStudent.getState() != null, TaskStudent::getState, taskStudent.getState());
        Page<TaskStudent> taskStudentPage = taskStudentService.page(page, queryWrapper);
        for (TaskStudent record : taskStudentPage.getRecords()) {
            Task task = taskService.getById(record.getTaskId());
            record.setTaskDescribe(task.getTaskDescribe());
            record.setNum(task.getNum());
            record.setImage(task.getImage());
        }
        return Result.success(taskStudentPage);
    }

    @PostMapping("getMyTaskList")
    public Result getMyTaskList(@RequestBody TaskStudent taskStudent) {
        User userInfo = ShiroUtils.getUserInfo();
        taskStudent.setUserId(userInfo.getId());
        QueryWrapper<TaskStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(taskStudent.getUserId()), TaskStudent::getUserId, taskStudent.getUserId())
                .eq(taskStudent.getState() != null, TaskStudent::getState, taskStudent.getState());
        List<TaskStudent> taskStudentList = taskStudentService.list(queryWrapper);
        for (TaskStudent record : taskStudentList) {
            Task task = taskService.getById(record.getTaskId());
            record.setTaskDescribe(task.getTaskDescribe());
            record.setNum(task.getNum());
            record.setImage(task.getImage());
        }
        return Result.success(taskStudentList);
    }

    /** 根据id获取课程报名 */
    @Log(name = "根据id获取课程报名", type = BusinessType.OTHER)
    @GetMapping("getTaskStudentById")
    public Result getTaskStudentById(@RequestParam("id")String id) {
        TaskStudent taskStudent = taskStudentService.getById(id);
        return Result.success(taskStudent);
    }

    /** 保存课程报名 */
    @Log(name = "保存课程报名", type = BusinessType.INSERT)
    @PostMapping("saveTaskStudent")
    public Result saveTaskStudent(@RequestBody TaskStudent taskStudent) {
        User userInfo = ShiroUtils.getUserInfo();
        taskStudent.setUserId(userInfo.getId());
        taskStudent.setUserName(userInfo.getUserName());
        Task task = taskService.getById(taskStudent.getTaskId());
        taskStudent.setTaskName(task.getName());
        taskStudent.setTeacherId(task.getTeacherId());
        taskStudent.setTeacherName(task.getTeacherName());
        boolean save = taskStudentService.save(taskStudent);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    @GetMapping("getTaskStudent")
    public Result getTaskStudent(@RequestParam("id") String id) {
        User userInfo = ShiroUtils.getUserInfo();
        QueryWrapper<TaskStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TaskStudent::getTaskId,id)
                .eq(TaskStudent::getUserId,userInfo.getId()).last("limit 1");
        TaskStudent taskStudent = taskStudentService.getOne(queryWrapper);
        if (taskStudent != null) {
            return Result.success(taskStudent.getState());
        } else {
            return Result.success(2);
        }
    }

    /** 编辑课程报名 */
    @Log(name = "编辑课程报名", type = BusinessType.UPDATE)
    @PostMapping("editTaskStudent")
    public Result editTaskStudent(@RequestBody TaskStudent taskStudent) {
        TaskStudent student = taskStudentService.getById(taskStudent.getId());
        Task task = taskService.getById(student.getTaskId());
        if (taskStudent.getState() == 0) {
            task.setNum(task.getNum() + 1);
        } else {
            task.setNum(task.getNum() - 1);
        }
        taskService.updateById(task);
        boolean save = taskStudentService.updateById(taskStudent);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除课程报名 */
    @GetMapping("removeTaskStudent")
    @Log(name = "删除课程报名", type = BusinessType.DELETE)
    public Result removeTaskStudent(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                taskStudentService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("课程报名id不能为空！");
        }
    }

    @PostMapping("getAdoptTaskStudent")
    public Result getAdoptTaskStudent(@RequestBody TaskStudent taskStudent) {
        Task task = taskService.getById(taskStudent.getTaskId());
        QueryWrapper<TaskStudent> query = new QueryWrapper<>();
        query.lambda().eq(TaskStudent::getTaskId, taskStudent.getTaskId())
                .eq(TaskStudent::getState,0);
        Page<TaskStudent> page = new Page<>(taskStudent.getPageNumber(), taskStudent.getPageSize());
        Page<TaskStudent> studentPage = taskStudentService.page(page, query);
        QueryWrapper<Chapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Chapter::getTaskId, taskStudent.getTaskId());
        int count2 = chapterService.count(queryWrapper);
        List<String> count3 = homeworkService.getTotalAssignCount(taskStudent.getTaskId());
        for (TaskStudent student : studentPage.getRecords()) {
            //获取考试得分
            Map<String,Object> score = testService.getStudentTotalScore(student.getTaskId(),student.getUserId());
            if (score != null) {
                student.setTestScore(score.get("point").toString());
                student.setTotalScore(score.get("total").toString());
            } else {
                student.setTestScore("0");
                student.setTotalScore("0");
            }
            //获取已看视频数量
            Integer count = chapterService.getStudentVideo(student.getTaskId(),student.getUserId());
            if (count == null) {
                count = 0;
            }
            student.setVideoCount(count);
            //获取已做作业数量
            List<String> count1 = homeworkService.getStudentHomeWork(student.getTaskId(),student.getUserId());
            student.setAssignCount(count1.size());
            student.setVideoNum(count2);
            student.setAssign(count3.size());
            student.setProportion(task.getProportion());
        }
        return Result.success(studentPage);
    }

    /*6.26 新增 学习进度*/
    @Autowired
    private LearningProgressService learningProgressService;

    @Log(name = "更新学习进度", type = BusinessType.OTHER)
    @PostMapping("updateLearningProgress")
    public Result updateLearningProgress(@RequestBody TaskStudent taskStudent) {
        User userInfo = ShiroUtils.getUserInfo();
        String userId = userInfo.getId();
        String taskId = taskStudent.getTaskId();
        System.out.println("taskId122:" + taskId);

        // 获取课程信息
        Task task = taskService.getById(taskId);
        if (task == null) {
            return Result.fail("课程不存在");
        }

        // 构造查询条件
        QueryWrapper<TaskStudent> query = new QueryWrapper<>();
        query.lambda().eq(TaskStudent::getTaskId, taskId)
                .eq(TaskStudent::getState, 0)
                .eq(TaskStudent::getUserId, userId);

        // 查询与登录用户名一致的数据
        TaskStudent student = taskStudentService.getOne(query);

        if (student == null) {
            return Result.fail("未找到与登录用户名一致的数据");
        }
        System.out.println("学生信息：" + student);

        // 获取考试得分
        Map<String, Object> score = testService.getStudentTotalScore(student.getTaskId(), student.getUserId());
        if (score != null) {
            student.setTestScore(score.get("point").toString());
            student.setTotalScore(score.get("total").toString());
        } else {
            student.setTestScore("0");
            student.setTotalScore("0");
        }

        // 获取已看视频数量
        Integer videoCount = chapterService.getStudentVideo(student.getTaskId(), student.getUserId());
        if (videoCount == null) {
            videoCount = 0;
        }
        student.setVideoCount(videoCount);

        // 获取已做作业数量
        List<String> assignList = homeworkService.getStudentHomeWork(student.getTaskId(), student.getUserId());
        student.setAssignCount(assignList.size());

        // 获取总视频数目
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.lambda().eq(Chapter::getTaskId, taskId);
        int totalVideos = chapterService.count(chapterQueryWrapper);
        student.setVideoNum(totalVideos);

        // 获取总作业数量
        List<String> totalAssignList = homeworkService.getTotalAssignCount(taskId);
        student.setAssign(totalAssignList.size());

        // 设置任务比例
        student.setProportion(task.getProportion());

        // 计算学习进度
        int totalItems = totalVideos + totalAssignList.size();
        int completedItems = videoCount + assignList.size();
        int progress = totalItems > 0 ? (completedItems * 100 / totalItems) : 0;

        // 创建或更新学习进度记录
        LearningProgress learningProgress = new LearningProgress();
        learningProgress.setUserId(userId);
        learningProgress.setProgress(progress);
        learningProgress.setUserName(userInfo.getUserName());
        learningProgress.setTeacherName(task.getTeacherName());
        learningProgress.setTaskName(task.getName());
        learningProgress.setUpdateTime(new Date());
        learningProgressService.updateOrInsertProgress(learningProgress);

        return Result.success(learningProgress);
    }
}