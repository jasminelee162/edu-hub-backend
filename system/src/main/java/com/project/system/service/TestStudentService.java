package com.project.system.service;

import com.project.system.domain.TestStudent;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 用户考试题目service
 * @date 2023/11/24 10:23
 */
public interface TestStudentService extends IService<TestStudent> {
    /*6.27 新增 薄弱科目*/
    List<Map<String, Object>> getStudentSubjectScores(String userId);

    /*6.28 新增 错题集*/
    List<TestStudent> getWrongAnswers(String userId);
}