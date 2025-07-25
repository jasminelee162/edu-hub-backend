package com.project.system.service.impl;

import com.project.system.domain.TaskComment;
import com.project.system.mapper.TaskCommentMapper;
import com.project.system.service.TaskCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 课程评论service实现类
 * @date 2023/11/21 08:12
 */
@Service
public class TaskCommentServiceImpl extends ServiceImpl<TaskCommentMapper, TaskComment> implements TaskCommentService {
}