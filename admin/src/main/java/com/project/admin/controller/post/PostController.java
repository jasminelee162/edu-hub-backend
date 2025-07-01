package com.project.admin.controller.post;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.system.domain.Post;
import com.project.system.service.PostService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @version 1.0
 * @description: 岗位controller
 */
@Controller
@ResponseBody
@RequestMapping("post")
public class PostController {

    @Autowired
    private PostService postService;

    /** 分页获取岗位 */
    @Log(name = "分页获取岗位", type = BusinessType.OTHER)
    @PostMapping("getPostPage")
    public Result getPostPage(@RequestBody Post post){
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(post.getPostName()), Post::getPostName, post.getPostName())
                .like(StringUtils.isNotBlank(post.getPostCode()), Post::getPostCode, post.getPostCode())
                .eq(post.getStatus() != null, Post::getStatus, post.getStatus()).orderByAsc(Post::getPostSort);
        Page<Post> page = new Page<>(post.getPageNumber(), post.getPageSize());
        Page<Post> postPage = postService.page(page, queryWrapper);
        return Result.success(postPage);
    }

    /** 获取岗位列表 */
    @Log(name = "获取岗位列表", type = BusinessType.OTHER)
    @PostMapping("getPostList")
    public Result getPostList(@RequestBody Post post) {
        QueryWrapper<Post> wrapper = new QueryWrapper<>();
        wrapper.lambda().like(StringUtils.isNotBlank(post.getPostName()), Post::getPostName, post.getPostName())
                .like(StringUtils.isNotBlank(post.getPostCode()), Post::getPostCode, post.getPostCode())
                .eq(post.getStatus() != null, Post::getStatus, post.getStatus()).orderByAsc(Post::getPostSort);
        List<Post> postList = postService.list(wrapper);
        return Result.success(postList);
    }

    /** 根据id获取岗位 */
    @Log(name = "根据id获取岗位", type = BusinessType.OTHER)
    @GetMapping("getPostById")
    public Result getPostById(@RequestParam("id")String id) {
        Post post = postService.getById(id);
        return Result.success(post);
    }

    /** 保存岗位 */
    @Log(name = "保存岗位", type = BusinessType.INSERT)
    @PostMapping("savePost")
    public Result savePost(@RequestBody Post post) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Post::getPostCode, post.getPostCode());
        int count = postService.count(queryWrapper);
        if (count > 0) {
            return Result.fail("岗位编码已存在！");
        }
        boolean save = postService.save(post);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑岗位 */
    @Log(name = "编辑岗位", type = BusinessType.UPDATE)
    @PostMapping("editPost")
    public Result editPost(@RequestBody Post post) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Post::getPostCode, post.getPostCode());
        int count = postService.count(queryWrapper);
        if (count > 0) {
            return Result.fail("岗位编码已存在！");
        }
        boolean update = postService.updateById(post);
        if (update) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除岗位 */
    @Log(name = "删除岗位", type = BusinessType.DELETE)
    @GetMapping("removePost")
    public Result removePost(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            List<String> asList = Arrays.asList(ids.split(","));
            boolean remove = postService.removeByIds(asList);
            if (remove) {
                return Result.success();
            } else {
                return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
            }
        } else {
            return Result.fail("岗位id不能为空！");
        }

    }

}
