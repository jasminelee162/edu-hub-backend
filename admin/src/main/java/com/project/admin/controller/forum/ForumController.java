package com.project.admin.controller.forum;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.framework.utils.ShiroUtils;
import com.project.system.domain.Forum;
import com.project.system.domain.User;
import com.project.system.service.ForumService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @version 1.0
 * @description: 论坛controller
 */
@Controller
@ResponseBody
@RequestMapping("forum")
public class ForumController {

    @Autowired
    private ForumService forumService;

    /** 分页获取论坛 */
    @Log(name = "分页获取论坛", type = BusinessType.OTHER)
    @PostMapping("getForumPage")
    public Result getForumPage(@RequestBody Forum forum) {
        Page<Forum> page = new Page<>(forum.getPageNumber(), forum.getPageSize());
        QueryWrapper<Forum> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(forum.getName()), Forum::getName, forum.getName())
                .like(StringUtils.isNotBlank(forum.getContent()), Forum::getContent, forum.getContent())
                .eq(StringUtils.isNotBlank(forum.getUserId()), Forum::getUserId, forum.getUserId())
                .orderByDesc(Forum::getCreateTime);
        Page<Forum> ForumPage = forumService.page(page, queryWrapper);
        return Result.success(ForumPage);
    }

    /** 根据id获取论坛 */
    @Log(name = "根据id获取论坛", type = BusinessType.OTHER)
    @GetMapping("getForumById")
    public Result getForumById(@RequestParam("id")String id) {
        Forum forum = forumService.getById(id);
        return Result.success(forum);
    }

    /** 保存论坛 */
    @Log(name = "保存论坛", type = BusinessType.INSERT)
    @PostMapping("saveForum")
    public Result saveForum(@RequestBody Forum forum) {
        User userInfo = ShiroUtils.getUserInfo();
        forum.setUserId(userInfo.getId());
        boolean save = forumService.save(forum);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑论坛 */
    @Log(name = "编辑论坛", type = BusinessType.UPDATE)
    @PostMapping("editForum")
    public Result editForum(@RequestBody Forum forum) {
        boolean save = forumService.updateById(forum);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除论坛 */
    @GetMapping("removeForum")
    @Log(name = "删除论坛", type = BusinessType.DELETE)
    public Result removeForum(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                forumService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("论坛id不能为空！");
        }
    }

}