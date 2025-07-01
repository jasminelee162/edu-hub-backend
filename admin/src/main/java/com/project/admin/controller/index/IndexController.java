package com.project.admin.controller.index;

import com.alibaba.fastjson2.JSONObject;
import com.project.common.domain.Result;
import com.project.framework.utils.ShiroUtils;
import com.project.system.domain.Task;
import com.project.system.domain.TaskStudent;
import com.project.system.domain.User;
import com.project.system.service.AccountService;
import com.project.system.service.TaskService;
import com.project.system.service.TaskStudentService;
import com.project.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @description: TODO
 */
@Controller
@ResponseBody
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskStudentService taskStudentService;
    @Autowired
    private AccountService accountService;

    /** 获取首页统计数据 */
    @GetMapping("getIndexAchievement")
    public Result getIndexAchievement() {
        int count = taskService.count();
        QueryWrapper<User> query = new QueryWrapper<>();
        query.lambda().eq(User::getUserType,1);
        int count1 = userService.count(query);
        QueryWrapper<User> query2 = new QueryWrapper<>();
        query.lambda().eq(User::getUserType,2);
        int count2 = userService.count(query2);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count",count);
        jsonObject.put("count1",count1);
        jsonObject.put("count2",count2);
        jsonObject.put("total",count1 + count2);
        return Result.success(jsonObject);
    }

    //后台首页

    @GetMapping("getIndexData")
    public Result getIndexData(@RequestParam("type") Integer type) {
        User userInfo = ShiroUtils.getUserInfo();
        JSONObject jsonObject = new JSONObject();
        //用户数量
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUserType,1).or()
                .eq(User::getUserType,2);
        int count = userService.count(queryWrapper);
        jsonObject.put("userNum",count);
        //教师数量
        QueryWrapper<User> query = new QueryWrapper<>();
        query.lambda().eq(User::getUserType,1);
        int count1 = userService.count(query);
        jsonObject.put("teacherNum",count1);
        if (type == 0) {
            //学生数量
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(User::getUserType,2);
            int num = userService.count(wrapper);
            jsonObject.put("studentNum",num);
        } else {
            //学生数量
            QueryWrapper<TaskStudent> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(TaskStudent::getTeacherId,userInfo.getId())
                    .eq(TaskStudent::getState,0).groupBy(TaskStudent::getUserId);
            List<TaskStudent> taskStudentList = taskStudentService.list(wrapper);
            jsonObject.put("studentNum",taskStudentList.size());
        }
        return Result.success(jsonObject);
    }

    @GetMapping("getIndexSexData")
    public Result getIndexSexData(@RequestParam("type") Integer type) {
        User userInfo = ShiroUtils.getUserInfo();
        List<JSONObject> list = new ArrayList<>();
        int nan = 0;
        int nv = 0;
        if (type == 0) {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(User::getUserType,2)
                    .eq(User::getSex,0);
            nan = userService.count(wrapper);
            QueryWrapper<User> wrapper1 = new QueryWrapper<>();
            wrapper1.lambda().eq(User::getUserType,2)
                    .eq(User::getSex,1);
            nv = userService.count(wrapper1);
        } else {
            QueryWrapper<TaskStudent> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(TaskStudent::getTeacherId,userInfo.getId())
                    .eq(TaskStudent::getState,0).groupBy(TaskStudent::getUserId);
            List<TaskStudent> taskStudentList = taskStudentService.list(wrapper);
            for (TaskStudent taskStudent : taskStudentList) {
                User user = userService.getById(taskStudent.getUserId());
                if (user.getSex() == 0) {
                    nan += 1;
                } else {
                    nv += 1;
                }
            }

        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value",nan);
        jsonObject.put("name","男");
        list.add(jsonObject);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("value",nv);
        jsonObject1.put("name","女");
        list.add(jsonObject1);
        return Result.success(list);
    }

    @GetMapping("getTaskChart")
    public Result getTaskChart(@RequestParam("type")Integer type) {
        User userInfo = ShiroUtils.getUserInfo();
        List<String> tasks = new ArrayList<>();
        List<Integer> nums =  new ArrayList<>();
        if (type == 0) {
            QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().orderByAsc(Task::getCreateTime).last("limit 10");
            List<Task> taskList = taskService.list(queryWrapper);
            for (Task task : taskList) {
                tasks.add(task.getName());
                QueryWrapper<TaskStudent> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(TaskStudent::getTaskId,task.getId())
                        .eq(TaskStudent::getState,0).groupBy(TaskStudent::getUserId);
                List<TaskStudent> list = taskStudentService.list(wrapper);
                nums.add(list.size());
            }
        } else {
            QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().orderByAsc(Task::getCreateTime).eq(Task::getTeacherId,userInfo.getId()).last("limit 10");
            List<Task> taskList = taskService.list(queryWrapper);
            for (Task task : taskList) {
                tasks.add(task.getName());
                QueryWrapper<TaskStudent> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(TaskStudent::getTaskId,task.getId())
                        .eq(TaskStudent::getState,0).groupBy(TaskStudent::getUserId);
                List<TaskStudent> list = taskStudentService.list(wrapper);
                nums.add(list.size());
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tasks",tasks);
        jsonObject.put("nums",nums);
        return Result.success(jsonObject);
    }

    @GetMapping("getTaskIndexList")
    public Result getTaskIndexList(@RequestParam("type")Integer type) {
        User userInfo = ShiroUtils.getUserInfo();
        List<Task> taskList = new ArrayList<>();
        if (type == 0) {
            QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().orderByDesc(Task::getCreateTime).last("limit 5");
            taskList = taskService.list(queryWrapper);
        } else {
            QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().orderByDesc(Task::getCreateTime).eq(Task::getTeacherId,userInfo.getId()).last("limit 5");
            taskList = taskService.list(queryWrapper);
        }
        return Result.success(taskList);
    }

}
