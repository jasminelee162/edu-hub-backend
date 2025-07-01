package com.project.system.service;

import com.project.system.domain.Homework;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 * @version 1.0
 * @description: 作业service
 * 
 */
public interface HomeworkService extends IService<Homework> {
    List<String> getStudentHomeWork(String taskId, String userId);

    List<String> getTotalAssignCount(String taskId);
}