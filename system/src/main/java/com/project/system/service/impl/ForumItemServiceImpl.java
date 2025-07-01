package com.project.system.service.impl;

import com.project.system.domain.ForumItem;
import com.project.system.mapper.ForumItemMapper;
import com.project.system.service.ForumItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 论坛讨论service实现类
 * @date 2024/01/18 09:21
 */
@Service
public class ForumItemServiceImpl extends ServiceImpl<ForumItemMapper, ForumItem> implements ForumItemService {
}