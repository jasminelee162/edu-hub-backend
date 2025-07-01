package com.project.system.service.impl;

import com.project.system.domain.ForumItem;
import com.project.system.mapper.ForumItemMapper;
import com.project.system.service.ForumItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 * @version 1.0
 * @description: 论坛讨论service实现类
 * 
 */
@Service
public class ForumItemServiceImpl extends ServiceImpl<ForumItemMapper, ForumItem> implements ForumItemService {
}