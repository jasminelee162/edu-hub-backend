package com.project.admin.controller.log;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.system.domain.LoginLog;
import com.project.system.service.LoginLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @version 1.0
 * @description: 登陆日志controller
 */
@Controller
@ResponseBody
@RequestMapping("loginLog")
public class LoginLogController {

    @Autowired
    private LoginLogService loginLogService;

    /** 查询 */
    @Log(name = "查询登陆日志", type = BusinessType.OTHER)
    @PostMapping("getLogPage")
    public Result getLogPage(@RequestBody LoginLog loginLog) {
        Page<LoginLog> page = new Page<>(loginLog.getPageNumber(), loginLog.getPageSize());
        QueryWrapper<LoginLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(loginLog.getUserName()), LoginLog::getUserName, loginLog.getUserName())
                .eq(loginLog.getStatus() != null, LoginLog::getStatus, loginLog.getStatus())
                .ge(loginLog.getStartTime() != null, LoginLog::getLoginTime, loginLog.getStartTime())
                .le(loginLog.getEndTime() != null, LoginLog::getLoginTime, loginLog.getEndTime())
                .orderByDesc(LoginLog::getLoginTime);
        Page<LoginLog> logPage = loginLogService.page(page, queryWrapper);
        return Result.success(logPage);
    }

    /** 删除 */
    @Log(name = "删除登陆日志", type = BusinessType.DELETE)
    @GetMapping("removeLog")
    public Result removeLog(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                loginLogService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("日志id不能为空！");
        }
    }

    /** 清空 */
    @Log(name = "清空登陆日志", type = BusinessType.DELETE)
    @GetMapping("clearLog")
    public Result clearLog() {
        boolean remove = loginLogService.remove(null);
        if (remove) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

}
