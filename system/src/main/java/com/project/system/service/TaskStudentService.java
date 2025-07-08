package com.project.system.service;

import com.project.system.domain.TaskStudent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 课程报名service
 * @date 2023/11/21 03:15
 */
public interface TaskStudentService extends IService<TaskStudent> {
    boolean unRead(String taskName);
    void checked(String taskName,String userName);

    int countDistinctUserByTaskId(String taskId) ;
}