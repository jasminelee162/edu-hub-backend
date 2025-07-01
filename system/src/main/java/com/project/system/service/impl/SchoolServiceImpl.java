package com.project.system.service.impl;

import com.project.system.domain.School;
import com.project.system.mapper.SchoolMapper;
import com.project.system.service.SchoolService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 * @version 1.0
 * @description: 学校表service实现类
 * 
 */
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements SchoolService {
}