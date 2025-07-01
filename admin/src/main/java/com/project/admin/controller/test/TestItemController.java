package com.project.admin.controller.test;

import com.alibaba.fastjson2.JSONObject;
import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.framework.utils.ShiroUtils;
import com.project.system.domain.Test;
import com.project.system.domain.TestItem;
import com.project.system.domain.TestStudent;
import com.project.system.domain.User;
import com.project.system.service.TestItemService;
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

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 考试题目controller
 * @date 2023/11/20 02:51
 */
@Controller
@ResponseBody
@RequestMapping("item")
public class TestItemController {

    @Autowired
    private TestItemService testItemService;
    @Autowired
    private TestService testService;
    @Autowired
    private TestStudentService testStudentService;

    /** 分页获取考试题目 */
    @Log(name = "分页获取考试题目", type = BusinessType.OTHER)
    @PostMapping("getTestItemPage")
    public Result getTestItemPage(@RequestBody TestItem testItem) {
        Page<TestItem> page = new Page<>(testItem.getPageNumber(), testItem.getPageSize());
        QueryWrapper<TestItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(testItem.getTitle()), TestItem::getTitle, testItem.getTitle())
                .eq(StringUtils.isNotBlank(testItem.getTestId()), TestItem::getTestId, testItem.getTestId());
        Page<TestItem> testItemPage = testItemService.page(page, queryWrapper);
        return Result.success(testItemPage);
    }

    /** 根据id获取考试题目 */
    @Log(name = "根据id获取考试题目", type = BusinessType.OTHER)
    @GetMapping("getTestItemById")
    public Result getTestItemById(@RequestParam("id")String id) {
        TestItem testItem = testItemService.getById(id);
        return Result.success(testItem);
    }

    @GetMapping("getTestItemByTestId")
    public Result getTestItemByTestId(@RequestParam("id")String id) {
        User userInfo = ShiroUtils.getUserInfo();
        Test test = testService.getById(id);
        QueryWrapper<TestItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TestItem::getTestId,id).orderByAsc(TestItem::getSort);
        List<TestItem> testItem = testItemService.list(queryWrapper);
        List<TestStudent> testStudents = new ArrayList<>();
        for (TestItem item : testItem) {
            QueryWrapper<TestStudent> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(TestStudent::getUserId,userInfo.getId())
                    .eq(TestStudent::getItemId,item.getId());
            TestStudent testStudent = testStudentService.getOne(wrapper);
            if (testStudent == null) {
                testStudent = new TestStudent();
                testStudent.setItemId(item.getId());
                testStudent.setTestId(item.getTestId());
                testStudent.setTitle(item.getTitle());
                testStudent.setContent(item.getContent());
                testStudent.setSort(item.getSort());
                testStudent.setType(item.getType());
                testStudent.setScore(item.getScore());
                testStudent.setKeyword(item.getKeyword());
                testStudent.setAnswer(item.getAnswer());
                testStudent.setUserId(userInfo.getId());
            }
            testStudents.add(testStudent);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("testItem", testStudents);
        jsonObject.put("test",test);
        return Result.success(jsonObject);
    }

    /** 保存考试题目 */
    @Log(name = "保存考试题目", type = BusinessType.INSERT)
    @PostMapping("saveTestItem")
    public Result saveTestItem(@RequestBody TestItem testItem) {
        boolean save = testItemService.save(testItem);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑考试题目 */
    @Log(name = "编辑考试题目", type = BusinessType.UPDATE)
    @PostMapping("editTestItem")
    public Result editTestItem(@RequestBody TestItem testItem) {
        boolean save = testItemService.updateById(testItem);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除考试题目 */
    @GetMapping("removeTestItem")
    @Log(name = "删除考试题目", type = BusinessType.DELETE)
    public Result removeTestItem(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                testItemService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("考试题目id不能为空！");
        }
    }

}