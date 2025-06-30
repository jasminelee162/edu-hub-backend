package com.project.admin.controller.test;

import com.alibaba.fastjson2.JSONObject;
import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.framework.utils.ShiroUtils;
import com.project.system.domain.*;
import com.project.system.service.TaskService;
import com.project.system.service.TaskStudentService;
import com.project.system.service.TestService;
import com.project.system.service.TestStudentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 考试controller
 * @date 2023/11/20 11:28
 */
@Controller
@ResponseBody
@RequestMapping("test")
public class TestController {

    @Autowired
    private TestService testService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskStudentService taskStudentService;
    @Autowired
    private TestStudentService testStudentService;

    /** 分页获取考试 */
    @Log(name = "分页获取考试", type = BusinessType.OTHER)
    @PostMapping("getApeTestPage")
    public Result getApeTestPage(@RequestBody Test test) {
        Page<Test> page = new Page<>(test.getPageNumber(), test.getPageSize());
        QueryWrapper<Test> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(test.getName()), Test::getName, test.getName())
                .like(StringUtils.isNotBlank(test.getTaskName()), Test::getTaskName, test.getTaskName())
                .eq(test.getState() != null, Test::getState, test.getState())
                .like(StringUtils.isNotBlank(test.getCreateBy()), Test::getCreateBy, test.getCreateBy());
        if (test.getType() == 1) {
            QueryWrapper<Task> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(Task::getTeacherId,ShiroUtils.getUserInfo().getId());
            List<Task> taskList = taskService.list(wrapper);
            List<String> list = new ArrayList<String>();
            for (Task task : taskList) {
                list.add(task.getId());
            }
            if (list.size()>0) {
                queryWrapper.lambda().in(Test::getTaskId,list);
            } else {
                list.add(" ");
                queryWrapper.lambda().in(Test::getTaskId,list);
            }
        }
        Page<Test> apeTestPage = testService.page(page, queryWrapper);
        return Result.success(apeTestPage);
    }

    /** 根据id获取考试 */
    @Log(name = "根据id获取考试", type = BusinessType.OTHER)
    @GetMapping("getApeTestById")
    public Result getApeTestById(@RequestParam("id")String id) {
        Test test = testService.getById(id);
        return Result.success(test);
    }

    /** 保存考试 */
    @Log(name = "保存考试", type = BusinessType.INSERT)
    @PostMapping("saveApeTest")
    public Result saveApeTest(@RequestBody Test test) {
        if (StringUtils.isNotBlank(test.getTaskId())) {
            Task task = taskService.getById(test.getTaskId());
            test.setTaskName(task.getName());
        }
        boolean save = testService.save(test);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑考试 */
    @Log(name = "编辑考试", type = BusinessType.UPDATE)
    @PostMapping("editApeTest")
    public Result editApeTest(@RequestBody Test test) {
        if (StringUtils.isNotBlank(test.getTaskId())) {
            Task task = taskService.getById(test.getTaskId());
            test.setTaskName(task.getName());
        }
        boolean save = testService.updateById(test);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除考试 */
    @GetMapping("removeApeTest")
    @Log(name = "删除考试", type = BusinessType.DELETE)
    public Result removeApeTest(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                testService.removeById(id);
                QueryWrapper<TestStudent> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(TestStudent::getTestId,id);
                testStudentService.remove(queryWrapper);
            }
            return Result.success();
        } else {
            return Result.fail("考试id不能为空！");
        }
    }

    /** 获取用户考试 */
    @GetMapping("getTestListByUser")
    public Result getTestListByUser() {
        User userInfo = ShiroUtils.getUserInfo();
        QueryWrapper<TaskStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TaskStudent::getUserId,userInfo.getId())
                .eq(TaskStudent::getState,0);
        List<TaskStudent> studentList = taskStudentService.list(queryWrapper);
        List<Test> apeTestList = new ArrayList<>();
        for (TaskStudent taskStudent : studentList) {
            QueryWrapper<Test> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(Test::getTaskId, taskStudent.getTaskId()).eq(Test::getState,0);
            List<Test> testList = testService.list(wrapper);
            if (testList.size() > 0) {
                apeTestList.addAll(testList);
            }
        }
        for (Test test : apeTestList) {
            QueryWrapper<TestStudent> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(TestStudent::getTestId,test.getId())
                    .eq(TestStudent::getUserId,userInfo.getId());
            List<TestStudent> list = testStudentService.list(queryWrapper1);
            if (list.size() <= 0) {
                test.setSchedule("未开始");
                test.setScoreTotal(0);
            } else {
                test.setSchedule("已完成");
                Integer total = 0;
                for (TestStudent testStudent : list) {
                    total += testStudent.getPoint();
                }
                test.setScoreTotal(total);
            }
        }
        return Result.success(apeTestList);
    }

    /*@PostMapping("getTestStudent")
    public Result getTestStudent(@RequestBody JSONObject jsonObject) {
        String testId = jsonObject.getString("testId");
        Test test = testService.getById(testId);
        String userName = jsonObject.getString("userName");
        Integer pageNumber = jsonObject.getInteger("pageNumber");
        Integer pageSize = jsonObject.getInteger("pageSize");
        Page<TestStudent> page = new Page<>(pageNumber,pageSize);
        QueryWrapper<TestStudent> queryWrapper =  new QueryWrapper<>();
        queryWrapper.lambda().eq(TestStudent::getTestId,testId)
                .like(StringUtils.isNotBlank(userName),TestStudent::getCreateBy,userName)
                .groupBy(TestStudent::getUserId).orderByAsc(TestStudent::getUpdateTime);
        Page<TestStudent> studentPage = testStudentService.page(page, queryWrapper);
        for (TestStudent student : studentPage.getRecords()) {
            QueryWrapper<TestStudent> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(TestStudent::getTestId,testId)
                            .eq(TestStudent::getUserId,student.getUserId());
            List<TestStudent> testStudents = testStudentService.list(wrapper);
            int score = 0;
            for (TestStudent item : testStudents) {
                score += item.getPoint();
            }
            student.setTestName(test.getName());
            student.setTotalScore(test.getTotalScore());
            student.setTotalGetScore(score);
        }
        return Result.success(studentPage);
    }*/
    @PostMapping("getTestStudent")
    public Result getTestStudent(@RequestBody JSONObject jsonObject) {
        String testId = jsonObject.getString("testId");
        Test test = testService.getById(testId);
        String userName = jsonObject.getString("userName");
        Integer pageNumber = jsonObject.getInteger("pageNumber");
        Integer pageSize = jsonObject.getInteger("pageSize");

        // 创建分页对象
        Page<TestStudent> page = new Page<>(pageNumber, pageSize);

        // 方案1：使用子查询先获取分组的用户ID列表
        // 1. 先查询分组的用户ID
        QueryWrapper<TestStudent> groupQuery = new QueryWrapper<>();
        groupQuery.select("user_id")
                .eq("test_id", testId)
                .groupBy("user_id")
                .orderByAsc("MIN(update_time)");

        if (StringUtils.isNotBlank(userName)) {
            groupQuery.like("create_by", userName);
        }

        // 2. 获取分页的用户ID列表
        Page<Map<String, Object>> userPage = testStudentService.pageMaps(
                new Page<>(pageNumber, pageSize),
                groupQuery
        );

        // 3. 收集用户ID
        List<String> userIds = userPage.getRecords().stream()
                .map(map -> (String)map.get("user_id"))
                .collect(Collectors.toList());

        // 4. 如果没有用户则返回空结果
        if (userIds.isEmpty()) {
            return Result.success(new Page<TestStudent>());
        }

        // 5. 查询这些用户的详细信息
        QueryWrapper<TestStudent> detailQuery = new QueryWrapper<>();
        detailQuery.in("user_id", userIds)
                .eq("test_id", testId);

        List<TestStudent> students = testStudentService.list(detailQuery);

        // 6. 按用户分组并计算总分
        Map<String, List<TestStudent>> grouped = students.stream()
                .collect(Collectors.groupingBy(TestStudent::getUserId));

        List<TestStudent> resultList = new ArrayList<>();
        for (Map.Entry<String, List<TestStudent>> entry : grouped.entrySet()) {
            TestStudent representative = entry.getValue().get(0); // 取第一条作为代表
            int totalScore = entry.getValue().stream()
                    .mapToInt(TestStudent::getPoint)
                    .sum();

            representative.setTestName(test.getName());
            representative.setTotalScore(test.getTotalScore());
            representative.setTotalGetScore(totalScore);
            resultList.add(representative);
        }

        // 7. 设置分页结果
        page.setRecords(resultList);
        page.setTotal(userPage.getTotal());

        return Result.success(page);
    }

}
