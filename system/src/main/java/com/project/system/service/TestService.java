package com.project.system.service;

import com.project.system.domain.Test;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 *
 * @version 1.0
 * @description: 考试service
 * 
 */
public interface TestService extends IService<Test> {
    Map<String,Object> getStudentTotalScore(String taskId, String userId);
}