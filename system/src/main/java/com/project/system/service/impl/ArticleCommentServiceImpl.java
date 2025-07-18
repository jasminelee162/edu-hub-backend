package com.project.system.service.impl;

import com.project.system.domain.ArticleComment;
import com.project.system.mapper.ArticleCommentMapper;
import com.project.system.service.ArticleCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 笔记评论service实现类
 * @date 2023/11/21 10:09
 */
@Service
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentMapper, ArticleComment> implements ArticleCommentService {
}