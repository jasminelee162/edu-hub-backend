package com.project.system.service.impl;

import com.project.system.domain.ArticleFavor;
import com.project.system.mapper.ArticleFavorMapper;
import com.project.system.service.ArticleFavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 笔记收藏service实现类
 * @date 2023/11/22 08:59
 */
@Service
public class ArticleFavorServiceImpl extends ServiceImpl<ArticleFavorMapper, ArticleFavor> implements ArticleFavorService {
}