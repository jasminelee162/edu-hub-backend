package com.project.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.project.system.domain.TaskStudent;
import com.project.system.mapper.TaskStudentMapper;
import com.project.system.service.TaskStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @version 1.0
 * @description: 课程报名service实现类
 *
 */
@Service
public class TaskStudentServiceImpl extends ServiceImpl<TaskStudentMapper, TaskStudent> implements TaskStudentService {
    @Autowired
    private TaskStudentMapper taskStudentMapper;

    public boolean unRead(String taskName){
        LambdaQueryWrapper<TaskStudent> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(TaskStudent::getTaskName, taskName);
        List<TaskStudent> studentTasks = taskStudentMapper.selectList(lambdaQuery);
        for(TaskStudent student:studentTasks){
            if(student.getChecked()==1){
                return true;
            }
        }
        return false;
    }

    public void checked(String taskName,String userName){
        TaskStudent updateEntity = new TaskStudent();
        updateEntity.setChecked(0);    // 要更新的分数
        LambdaUpdateWrapper<TaskStudent> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(TaskStudent::getTaskName, taskName)
                .eq(TaskStudent::getUserName, userName);


        taskStudentMapper.update(updateEntity, updateWrapper);
    }
}