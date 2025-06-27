package com.ape.apesystem.service;

import com.ape.apesystem.domain.ApeTestStudent;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 用户考试题目service
 * @date 2023/11/24 10:23
 */
public interface ApeTestStudentService extends IService<ApeTestStudent> {
    /*6.27 新增 薄弱科目*/
    List<Map<String, Object>> getStudentSubjectScores(String userId);

    /*6.28 新增 错题集*/
    List<ApeTestStudent> getWrongAnswers(String userId);
}