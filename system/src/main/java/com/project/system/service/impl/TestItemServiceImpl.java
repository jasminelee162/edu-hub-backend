package com.project.system.service.impl;

import com.project.system.domain.TestItem;
import com.project.system.mapper.TestItemMapper;
import com.project.system.service.TestItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 考试题目service实现类
 * @date 2023/11/20 02:51
 */
@Service
public class TestItemServiceImpl extends ServiceImpl<TestItemMapper, TestItem> implements TestItemService {
}