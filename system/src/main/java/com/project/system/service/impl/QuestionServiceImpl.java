package com.project.system.service.impl;

import com.project.system.domain.Question;
import com.project.system.mapper.QuestionMapper;
import com.project.system.service.QuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 答疑service实现类
 * @date 2024/01/18 11:14
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {
}