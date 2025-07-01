package com.project.admin.controller.account;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.system.domain.Account;
import com.project.system.service.AccountService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 公告controller
 * @date 2023/9/21 8:48
 */
@Controller
@ResponseBody
@RequestMapping("account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /** 分页查询公告 */
    @Log(name = "分页查询公告", type = BusinessType.OTHER)
    @PostMapping("getAccountPage")
    public Result getAccountPage(@RequestBody Account account) {
        Page<Account> page = new Page<>(account.getPageNumber(), account.getPageSize());
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(account.getTitle()), Account::getTitle, account.getTitle())
                .like(StringUtils.isNotBlank(account.getUpdateBy()), Account::getUpdateBy, account.getUpdateBy())
                .eq(account.getType() != null, Account::getType, account.getType());
        Page<Account> accountPage = accountService.page(page, queryWrapper);
        return Result.success(accountPage);
    }

    /** 根据id查询公告 */
    @GetMapping("getById")
    @Log(name = "根据id查询公告", type = BusinessType.OTHER)
    public Result getById(@RequestParam("id") String id) {
        Account account = accountService.getById(id);
        return Result.success(account);
    }

    /** 保存 */
    @PostMapping("saveAccount")
    @Log(name = "保存公告", type = BusinessType.INSERT)
    public Result saveAccount(@RequestBody Account account) {
        boolean save = accountService.save(account);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑 */
    @PostMapping("editAccount")
    @Log(name = "编辑公告", type = BusinessType.UPDATE)
    public Result editDept(@RequestBody Account account){
        boolean edit = accountService.updateById(account);
        if (edit) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除 */
    @GetMapping("removeAccount")
    @Log(name = "删除公告", type = BusinessType.DELETE)
    public Result removeDept(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                accountService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("公告id不能为空！");
        }
    }

}
