package com.project.system.service.impl;

import com.project.system.domain.HomeworkStudent;
import com.project.system.mapper.HomeworkStudentMapper;
import com.project.system.service.HomeworkStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 * @version 1.0
 * @description: 学生作业service实现类
 * 
 */
@Service
public class HomeworkStudentServiceImpl extends ServiceImpl<HomeworkStudentMapper, HomeworkStudent> implements HomeworkStudentService {
}