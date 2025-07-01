package com.project.system.service.impl;

import com.project.system.domain.Classification;
import com.project.system.mapper.ClassificationMapper;
import com.project.system.service.ClassificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 * @version 1.0
 * @description: 分类service实现类
 * 
 */
@Service
public class ClassificationServiceImpl extends ServiceImpl<ClassificationMapper, Classification> implements ClassificationService {
}