package com.project.system.service.impl;

import com.project.system.domain.Test;
import com.project.system.mapper.TestMapper;
import com.project.system.service.TestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 考试service实现类
 * @date 2023/11/20 11:28
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {
    @Override
    public Map<String,Object> getStudentTotalScore(String taskId, String userId) {
        return baseMapper.getStudentTotalScore(taskId,userId);
    }
}