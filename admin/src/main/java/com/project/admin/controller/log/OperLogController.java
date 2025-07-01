package com.project.admin.controller.log;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.system.domain.OperateLog;
import com.project.system.service.OperateLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @version 1.0
 * @description: 操作日志controller
 */
@Controller
@ResponseBody
@RequestMapping("operLog")
public class OperLogController {

    @Autowired
    private OperateLogService operateLogService;

    /** 查询 */
    @Log(name = "查询操作日志", type = BusinessType.OTHER)
    @PostMapping("getLogPage")
    public Result getLogPage(@RequestBody OperateLog operateLog) {
        Page<OperateLog> page = new Page<>(operateLog.getPageNumber(), operateLog.getPageSize());
        QueryWrapper<OperateLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(operateLog.getType()!=null, OperateLog::getType, operateLog.getType())
                .like(StringUtils.isNotBlank(operateLog.getOperUserAccount()), OperateLog::getOperUserAccount, operateLog.getOperUserAccount())
                .ge(operateLog.getStartTime() != null, OperateLog::getOperTime, operateLog.getStartTime())
                .le(operateLog.getEndTime() != null, OperateLog::getOperTime, operateLog.getEndTime())
                .orderByDesc(OperateLog::getOperTime);
        Page<OperateLog> logPage = operateLogService.page(page, queryWrapper);
        return Result.success(logPage);
    }

    /** 删除 */
    @Log(name = "删除操作日志", type = BusinessType.DELETE)
    @GetMapping("removeLog")
    public Result removeLog(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                operateLogService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("日志id不能为空！");
        }
    }

    /** 清空 */
    @Log(name = "清空操作日志", type = BusinessType.DELETE)
    @GetMapping("clearLog")
    public Result clearLog() {
        boolean remove = operateLogService.remove(null);
        if (remove) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

}
