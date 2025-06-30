package com.project.system.service;

import com.project.system.domain.Test;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 考试service
 * @date 2023/11/20 11:28
 */
public interface TestService extends IService<Test> {
    Map<String,Object> getStudentTotalScore(String taskId, String userId);
}