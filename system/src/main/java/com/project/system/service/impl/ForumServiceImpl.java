package com.project.system.service.impl;

import com.project.system.domain.Forum;
import com.project.system.mapper.ForumMapper;
import com.project.system.service.ForumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 论坛service实现类
 * @date 2024/01/18 08:55
 */
@Service
public class ForumServiceImpl extends ServiceImpl<ForumMapper, Forum> implements ForumService {
}