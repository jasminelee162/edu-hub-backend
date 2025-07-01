package com.project.admin.controller.article;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.system.domain.Article;
import com.project.system.domain.ArticleFavor;
import com.project.system.service.ArticleFavorService;
import com.project.system.service.ArticleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @version 1.0
 * @description: 笔记收藏controller
 */
@Controller
@ResponseBody
@RequestMapping("favor")
public class ArticleFavorController {

    @Autowired
    private ArticleFavorService articleFavorService;
    @Autowired
    private ArticleService articleService;

    /** 分页获取笔记收藏 */
    @Log(name = "分页获取笔记收藏", type = BusinessType.OTHER)
    @PostMapping("getArticleFavorPage")
    public Result getArticleFavorPage(@RequestBody ArticleFavor articleFavor) {
        Page<ArticleFavor> page = new Page<>(articleFavor.getPageNumber(), articleFavor.getPageSize());
        QueryWrapper<ArticleFavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(articleFavor.getArticleId()), ArticleFavor::getArticleId, articleFavor.getArticleId())
                .eq(StringUtils.isNotBlank(articleFavor.getUserId()), ArticleFavor::getUserId, articleFavor.getUserId());
        Page<ArticleFavor> articleFavorPage = articleFavorService.page(page, queryWrapper);
        for (ArticleFavor record : articleFavorPage.getRecords()) {
            Article article = articleService.getById(record.getArticleId());
            record.setTitle(article.getTitle());
            record.setCreateBy(article.getCreateBy());
            record.setCreateTime(article.getCreateTime());
            record.setAvatar(article.getAvatar());
            record.setArticleDesc(article.getArticleDesc());
        }
        return Result.success(articleFavorPage);
    }

    /** 根据id获取笔记收藏 */
    @Log(name = "根据id获取笔记收藏", type = BusinessType.OTHER)
    @GetMapping("getArticleFavorById")
    public Result getArticleFavorById(@RequestParam("id")String id) {
        ArticleFavor articleFavor = articleFavorService.getById(id);
        return Result.success(articleFavor);
    }

    /** 保存笔记收藏 */
    @Log(name = "保存笔记收藏", type = BusinessType.INSERT)
    @PostMapping("saveArticleFavor")
    public Result saveArticleFavor(@RequestBody ArticleFavor articleFavor) {
        boolean save = articleFavorService.save(articleFavor);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑笔记收藏 */
    @Log(name = "编辑笔记收藏", type = BusinessType.UPDATE)
    @PostMapping("editArticleFavor")
    public Result editArticleFavor(@RequestBody ArticleFavor articleFavor) {
        boolean save = articleFavorService.updateById(articleFavor);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除笔记收藏 */
    @PostMapping("removeArticleFavor")
    @Log(name = "删除笔记收藏", type = BusinessType.DELETE)
    public Result removeArticleFavor(@RequestBody ArticleFavor articleFavor) {
        QueryWrapper<ArticleFavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ArticleFavor::getArticleId, articleFavor.getArticleId())
                .eq(ArticleFavor::getUserId, articleFavor.getUserId());
        boolean remove = articleFavorService.remove(queryWrapper);
        if (remove) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

}