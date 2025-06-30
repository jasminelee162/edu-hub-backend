package com.project.admin.controller.article;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.framework.utils.ShiroUtils;
import com.project.system.domain.ArticleComment;
import com.project.system.domain.User;
import com.project.system.service.ArticleCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 笔记评论controller
 * @date 2023/11/21 10:09
 */
@Controller
@ResponseBody
@RequestMapping("articleComment")
public class ArticleCommentController {

    @Autowired
    private ArticleCommentService articleCommentService;

    /** 分页获取笔记评论 */
    @Log(name = "分页获取笔记评论", type = BusinessType.OTHER)
    @PostMapping("getApeArticleCommentPage")
    public Result getApeArticleCommentPage(@RequestBody ArticleComment articleComment) {
        Page<ArticleComment> page = new Page<>(articleComment.getPageNumber(), articleComment.getPageSize());
        QueryWrapper<ArticleComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(articleComment.getTaskId()), ArticleComment::getTaskId, articleComment.getTaskId())
                .like(StringUtils.isNotBlank(articleComment.getContent()), ArticleComment::getContent, articleComment.getContent())
                .like(StringUtils.isNotBlank(articleComment.getCreateBy()), ArticleComment::getCreateBy, articleComment.getCreateBy());
        Page<ArticleComment> apeArticleCommentPage = articleCommentService.page(page, queryWrapper);
        return Result.success(apeArticleCommentPage);
    }

    /** 根据id获取笔记评论 */
    @Log(name = "根据id获取笔记评论", type = BusinessType.OTHER)
    @GetMapping("getApeArticleCommentById")
    public Result getApeArticleCommentById(@RequestParam("id")String id) {
        ArticleComment articleComment = articleCommentService.getById(id);
        return Result.success(articleComment);
    }

    @GetMapping("getApeArticleCommentByArticleId")
    public Result getApeArticleCommentByArticleId(@RequestParam("id")String id) {
        QueryWrapper<ArticleComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ArticleComment::getTaskId,id).orderByDesc(ArticleComment::getCreateTime);
        List<ArticleComment> commentList = articleCommentService.list(queryWrapper);
        return Result.success(commentList);
    }

    /** 保存笔记评论 */
    @Log(name = "保存笔记评论", type = BusinessType.INSERT)
    @PostMapping("saveApeArticleComment")
    public Result saveApeArticleComment(@RequestBody ArticleComment articleComment) {
        User userInfo = ShiroUtils.getUserInfo();
        articleComment.setAvatar(userInfo.getAvatar());
        boolean save = articleCommentService.save(articleComment);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑笔记评论 */
    @Log(name = "编辑笔记评论", type = BusinessType.UPDATE)
    @PostMapping("editApeArticleComment")
    public Result editApeArticleComment(@RequestBody ArticleComment articleComment) {
        boolean save = articleCommentService.updateById(articleComment);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除笔记评论 */
    @GetMapping("removeApeArticleComment")
    @Log(name = "删除笔记评论", type = BusinessType.DELETE)
    public Result removeApeArticleComment(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                articleCommentService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("笔记评论id不能为空！");
        }
    }

}