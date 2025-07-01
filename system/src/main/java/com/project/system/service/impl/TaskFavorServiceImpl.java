package com.project.system.service.impl;

import com.project.system.domain.TaskFavor;
import com.project.system.mapper.TaskFavorMapper;
import com.project.system.service.TaskFavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 * @version 1.0
 * @description: 课程收藏service实现类
 * 
 */
@Service
public class TaskFavorServiceImpl extends ServiceImpl<TaskFavorMapper, TaskFavor> implements TaskFavorService {
}