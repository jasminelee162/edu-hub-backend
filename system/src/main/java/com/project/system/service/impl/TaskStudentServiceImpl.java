package com.project.system.service.impl;

import com.project.system.domain.TaskStudent;
import com.project.system.mapper.TaskStudentMapper;
import com.project.system.service.TaskStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 课程报名service实现类
 * @date 2023/11/21 03:15
 */
@Service
public class TaskStudentServiceImpl extends ServiceImpl<TaskStudentMapper, TaskStudent> implements TaskStudentService {
}