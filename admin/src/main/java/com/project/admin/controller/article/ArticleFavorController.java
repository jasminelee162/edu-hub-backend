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
 * @author 超级管理员
 * @version 1.0
 * @description: 笔记收藏controller
 * @date 2023/11/22 08:59
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
    @PostMapping("getApeArticleFavorPage")
    public Result getApeArticleFavorPage(@RequestBody ArticleFavor apeArticleFavor) {
        Page<ArticleFavor> page = new Page<>(apeArticleFavor.getPageNumber(),apeArticleFavor.getPageSize());
        QueryWrapper<ArticleFavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(apeArticleFavor.getArticleId()), ArticleFavor::getArticleId,apeArticleFavor.getArticleId())
                .eq(StringUtils.isNotBlank(apeArticleFavor.getUserId()), ArticleFavor::getUserId,apeArticleFavor.getUserId());
        Page<ArticleFavor> apeArticleFavorPage = articleFavorService.page(page, queryWrapper);
        for (ArticleFavor articleFavor : apeArticleFavorPage.getRecords()) {
            Article article = articleService.getById(articleFavor.getArticleId());
            articleFavor.setTitle(article.getTitle());
            articleFavor.setCreateBy(article.getCreateBy());
            articleFavor.setCreateTime(article.getCreateTime());
            articleFavor.setAvatar(article.getAvatar());
            articleFavor.setArticleDesc(article.getArticleDesc());
        }
        return Result.success(apeArticleFavorPage);
    }

    /** 根据id获取笔记收藏 */
    @Log(name = "根据id获取笔记收藏", type = BusinessType.OTHER)
    @GetMapping("getApeArticleFavorById")
    public Result getApeArticleFavorById(@RequestParam("id")String id) {
        ArticleFavor articleFavor = articleFavorService.getById(id);
        return Result.success(articleFavor);
    }

    /** 保存笔记收藏 */
    @Log(name = "保存笔记收藏", type = BusinessType.INSERT)
    @PostMapping("saveApeArticleFavor")
    public Result saveApeArticleFavor(@RequestBody ArticleFavor articleFavor) {
        boolean save = articleFavorService.save(articleFavor);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑笔记收藏 */
    @Log(name = "编辑笔记收藏", type = BusinessType.UPDATE)
    @PostMapping("editApeArticleFavor")
    public Result editApeArticleFavor(@RequestBody ArticleFavor articleFavor) {
        boolean save = articleFavorService.updateById(articleFavor);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除笔记收藏 */
    @PostMapping("removeApeArticleFavor")
    @Log(name = "删除笔记收藏", type = BusinessType.DELETE)
    public Result removeApeArticleFavor(@RequestBody ArticleFavor articleFavor) {
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