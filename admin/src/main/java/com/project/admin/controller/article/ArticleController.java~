package com.project.admin.controller.article;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.framework.utils.ShiroUtils;
import com.project.system.domain.*;
import com.project.system.service.ArticleCommentService;
import com.project.system.service.ArticleFavorService;
import com.project.system.service.ArticleService;
import com.project.system.service.TaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 笔记controller
 * @date 2023/11/20 09:14
 */
@Controller
@ResponseBody
@RequestMapping("article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ArticleFavorService articleFavorService;
    @Autowired
    private ArticleCommentService articleCommentService;

    /** 分页获取笔记 */
    @Log(name = "分页获取笔记", type = BusinessType.OTHER)
    @PostMapping("getArticlePage")
    public Result getArticlePage(@RequestBody Article article) {
        Page<Article> page = new Page<>(article.getPageNumber(), article.getPageSize());
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(article.getUserId()), Article::getUserId, article.getUserId())
                .like(StringUtils.isNotBlank(article.getTitle()), Article::getTitle, article.getTitle())
                .eq(article.getState() != null, Article::getState, article.getState())
                .like(StringUtils.isNotBlank(article.getTaskName()), Article::getTaskName, article.getTaskName());
        if (article.getType() == 1) {
            QueryWrapper<Task> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(Task::getTeacherId,ShiroUtils.getUserInfo().getId());
            List<Task> taskList = taskService.list(wrapper);
            List<String> list = new ArrayList<String>();
            for (Task task : taskList) {
                list.add(task.getId());
            }
            if (list.size()>0) {
                queryWrapper.lambda().in(Article::getTaskId,list);
            } else {
                list.add(" ");
                queryWrapper.lambda().in(Article::getTaskId,list);
            }
        }
        Page<Article> apeArticlePage = articleService.page(page, queryWrapper);
        return Result.success(apeArticlePage);
    }

    @GetMapping("getIndexArticleList")
    public Result getIndexArticleList() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().last("limit 2");
        List<Article> articleList = articleService.list(queryWrapper);
        return Result.success(articleList);
    }

    /** 根据id获取笔记 */
    @Log(name = "根据id获取笔记", type = BusinessType.OTHER)
    @GetMapping("getArticleById")
    public Result getArticleById(@RequestParam("id")String id) {
        Article article = articleService.getById(id);
        User userInfo = ShiroUtils.getUserInfo();
        QueryWrapper<ArticleFavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ArticleFavor::getArticleId,id).eq(ArticleFavor::getUserId,userInfo.getId());
        ArticleFavor favor = articleFavorService.getOne(queryWrapper);
        if (favor == null) {
            article.setFavor(0);
        } else {
            article.setFavor(1);
        }
        return Result.success(article);
    }

    /** 保存笔记 */
    @Log(name = "保存笔记", type = BusinessType.INSERT)
    @PostMapping("saveArticle")
    public Result saveArticle(@RequestBody Article article) {
        User userInfo = ShiroUtils.getUserInfo();
        article.setUserId(userInfo.getId());
        article.setAvatar(userInfo.getAvatar());
        if (StringUtils.isNotBlank(article.getTaskId())) {
            Task task = taskService.getById(article.getTaskId());
            article.setTaskName(task.getName());
        }
        boolean save = articleService.save(article);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑笔记 */
    @Log(name = "编辑笔记", type = BusinessType.UPDATE)
    @PostMapping("editArticle")
    public Result editArticle(@RequestBody Article article) {
        User userInfo = ShiroUtils.getUserInfo();
        article.setAvatar(userInfo.getAvatar());
        if (StringUtils.isNotBlank(article.getTaskId())) {
            Task task = taskService.getById(article.getTaskId());
            article.setTaskName(task.getName());
        }
        boolean save = articleService.updateById(article);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除笔记 */
    @GetMapping("removeArticle")
    @Log(name = "删除笔记", type = BusinessType.DELETE)
    public Result removeArticle(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                articleService.removeById(id);
                QueryWrapper<ArticleComment> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(ArticleComment::getTaskId,id);
                articleCommentService.remove(queryWrapper);
                QueryWrapper<ArticleFavor> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.lambda().eq(ArticleFavor::getArticleId,id);
                articleFavorService.remove(queryWrapper2);
            }
            return Result.success();
        } else {
            return Result.fail("笔记id不能为空！");
        }
    }

}