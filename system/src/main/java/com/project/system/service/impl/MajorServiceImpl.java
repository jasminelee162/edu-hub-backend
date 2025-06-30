package com.project.system.service.impl;

import com.project.system.domain.Major;
import com.project.system.mapper.MajorMapper;
import com.project.system.service.MajorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 专业表service实现类
 * @date 2023/11/16 08:31
 */
@Service
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major> implements MajorService {
}