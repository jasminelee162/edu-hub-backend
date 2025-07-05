package com.project.system.service;

import com.project.system.domain.HomeworkStudent;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 学生作业service
 * @date 2023/11/22 04:28
 */
public interface HomeworkStudentService extends IService<HomeworkStudent> {
    List<HomeworkStudent> selectOnePerChapter(String userId);
}