package com.project.system.service.impl;

import com.project.system.domain.Article;
import com.project.system.mapper.ArticleMapper;
import com.project.system.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 笔记service实现类
 * @date 2023/11/20 09:14
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
}