package com.project.system.service.impl;

import com.project.system.domain.Post;
import com.project.system.mapper.ePostMapper;
import com.project.system.service.PostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 * @version 1.0
 * @description: 岗位service实现类
 * 
 */
@Service
public class PostServiceImpl extends ServiceImpl<ePostMapper, Post> implements PostService {
}
