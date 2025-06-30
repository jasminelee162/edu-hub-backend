package com.project.system.service.impl;

import com.project.system.domain.Task;
import com.project.system.mapper.TaskMapper;
import com.project.system.service.TaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 课程service实现类
 * @date 2023/11/17 11:28
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
}