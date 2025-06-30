package com.project.admin.controller.message;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.framework.utils.ShiroUtils;
import com.project.system.domain.Message;
import com.project.system.domain.User;
import com.project.system.service.MessageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 留言表controller
 * @date 2023/11/16 08:50
 */
@Controller
@ResponseBody
@RequestMapping("message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /** 分页获取留言表 */
    @Log(name = "分页获取留言表", type = BusinessType.OTHER)
    @PostMapping("getMessagePage")
    public Result getMessagePage(@RequestBody Message message) {
        if (message.getFlag() == 0) {
            User userInfo = ShiroUtils.getUserInfo();
            message.setUserId(userInfo.getId());
        }
        Page<Message> page = new Page<>(message.getPageNumber(), message.getPageSize());
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(message.getUserId()), Message::getUserId, message.getUserId())
                .like(StringUtils.isNotBlank(message.getUserName()), Message::getUserName, message.getUserName())
                .like(StringUtils.isNotBlank(message.getContent()), Message::getContent, message.getContent())
                .like(StringUtils.isNotBlank(message.getAnswer()), Message::getAnswer, message.getAnswer());
        Page<Message> messagePage = messageService.page(page, queryWrapper);
        return Result.success(messagePage);
    }

    /** 根据id获取留言表 */
    @Log(name = "根据id获取留言表", type = BusinessType.OTHER)
    @GetMapping("getMessageById")
    public Result getMessageById(@RequestParam("id")String id) {
        Message message = messageService.getById(id);
        return Result.success(message);
    }

    /** 保存留言表 */
    @Log(name = "保存留言表", type = BusinessType.INSERT)
    @PostMapping("saveMessage")
    public Result saveMessage(@RequestBody Message message) {
        User userInfo = ShiroUtils.getUserInfo();
        message.setUserId(userInfo.getId());
        message.setUserName(userInfo.getUserName());
        boolean save = messageService.save(message);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑留言表 */
    @Log(name = "编辑留言表", type = BusinessType.UPDATE)
    @PostMapping("editMessage")
    public Result editMessage(@RequestBody Message message) {
        boolean save = messageService.updateById(message);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除留言表 */
    @GetMapping("removeMessage")
    @Log(name = "删除留言表", type = BusinessType.DELETE)
    public Result removeMessage(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                messageService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("留言表id不能为空！");
        }
    }

}