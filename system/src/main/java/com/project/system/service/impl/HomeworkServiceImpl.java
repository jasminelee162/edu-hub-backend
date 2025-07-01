package com.project.system.service.impl;

import com.project.system.domain.Homework;
import com.project.system.mapper.HomeworkMapper;
import com.project.system.service.HomeworkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @version 1.0
 * @description: 作业service实现类
 * 
 */
@Service
public class HomeworkServiceImpl extends ServiceImpl<HomeworkMapper, Homework> implements HomeworkService {
    @Override
    public List<String> getStudentHomeWork(String taskId, String userId) {
        return baseMapper.getStudentHomeWork(taskId, userId);
    }

    @Override
    public List<String> getTotalAssignCount(String taskId) {
        return baseMapper.getTotalAssignCount(taskId);
    }
}