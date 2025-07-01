package com.project.system.service.impl;

import com.project.system.domain.TestItem;
import com.project.system.mapper.TestItemMapper;
import com.project.system.service.TestItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 * @version 1.0
 * @description: 考试题目service实现类
 * 
 */
@Service
public class TestItemServiceImpl extends ServiceImpl<TestItemMapper, TestItem> implements TestItemService {
}