package com.project.admin.controller.forum;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.framework.utils.ShiroUtils;
import com.project.system.domain.ForumItem;
import com.project.system.domain.User;
import com.project.system.service.ForumItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version 1.0
 * @description: 论坛讨论controller
 */
@Controller
@ResponseBody
@RequestMapping("item")
public class ForumItemController {

    @Autowired
    private ForumItemService forumItemService;

    /** 分页获取论坛讨论 */
    @Log(name = "分页获取论坛讨论", type = BusinessType.OTHER)
    @PostMapping("getForumItemPage")
    public Result getForumItemPage(@RequestBody ForumItem forumItem) {
        Page<ForumItem> page = new Page<>(forumItem.getPageNumber(), forumItem.getPageSize());
        QueryWrapper<ForumItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(forumItem.getForumId()), ForumItem::getForumId, forumItem.getForumId())
                .like(StringUtils.isNotBlank(forumItem.getUserName()), ForumItem::getUserName, forumItem.getUserName())
                .eq(StringUtils.isNotBlank(forumItem.getCreateBy()), ForumItem::getCreateBy, forumItem.getCreateBy())
                .eq(forumItem.getCreateTime() != null, ForumItem::getCreateTime, forumItem.getCreateTime())
                .orderByDesc(ForumItem::getCreateTime);
        Page<ForumItem> forumItemPage = forumItemService.page(page, queryWrapper);
        return Result.success(forumItemPage);
    }

    @PostMapping("getForumItemList")
    public Result getForumItemList(@RequestBody ForumItem forumItem) {
        QueryWrapper<ForumItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(forumItem.getForumId()), ForumItem::getForumId, forumItem.getForumId())
                .like(StringUtils.isNotBlank(forumItem.getUserName()), ForumItem::getUserName, forumItem.getUserName())
                .eq(StringUtils.isNotBlank(forumItem.getCreateBy()), ForumItem::getCreateBy, forumItem.getCreateBy())
                .eq(forumItem.getCreateTime() != null, ForumItem::getCreateTime, forumItem.getCreateTime())
                .orderByDesc(ForumItem::getCreateTime);
        List<ForumItem> forumItemPage = forumItemService.list(queryWrapper);
        return Result.success(forumItemPage);
    }

    /** 根据id获取论坛讨论 */
    @Log(name = "根据id获取论坛讨论", type = BusinessType.OTHER)
    @GetMapping("getForumItemById")
    public Result getForumItemById(@RequestParam("id")String id) {
        ForumItem forumItem = forumItemService.getById(id);
        return Result.success(forumItem);
    }

    /** 保存论坛讨论 */
    @Log(name = "保存论坛讨论", type = BusinessType.INSERT)
    @PostMapping("saveForumItem")
    public Result saveForumItem(@RequestBody ForumItem forumItem) {
        User userInfo = ShiroUtils.getUserInfo();
        forumItem.setUserId(userInfo.getId());
        forumItem.setUserAvatar(userInfo.getAvatar());
        forumItem.setUserName(userInfo.getUserName());
        boolean save = forumItemService.save(forumItem);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑论坛讨论 */
    @Log(name = "编辑论坛讨论", type = BusinessType.UPDATE)
    @PostMapping("editForumItem")
    public Result editForumItem(@RequestBody ForumItem forumItem) {
        boolean save = forumItemService.updateById(forumItem);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除论坛讨论 */
    @GetMapping("removeForumItem")
    @Log(name = "删除论坛讨论", type = BusinessType.DELETE)
    public Result removeForumItem(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                forumItemService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("论坛讨论id不能为空！");
        }
    }

}