package com.project.admin.controller.param;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.system.domain.Param;
import com.project.system.service.ParamService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @version 1.0
 * @description: 参数controller
 */
@Controller
@ResponseBody
@RequestMapping("param")
public class ParamController {

    @Autowired
    private ParamService paramService;

    /** 分页获取参数列表 */
    @Log(name = "分页获取参数列表", type = BusinessType.OTHER)
    @PostMapping("getParamPage")
    public Result getParamPage(@RequestBody Param param) {
        Page<Param> page = new Page<>(param.getPageNumber(), param.getPageSize());
        QueryWrapper<Param> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(param.getParamName()), Param::getParamName, param.getParamName())
                .like(StringUtils.isNotBlank(param.getParamKey()), Param::getParamKey, param.getParamKey())
                .eq(param.getWithin() != null, Param::getWithin, param.getWithin());
        Page<Param> apeParamPage = paramService.page(page, queryWrapper);
        return Result.success(apeParamPage);
    }

    /** 根据id获取参数 */
    @Log(name = "根据id获取参数", type = BusinessType.OTHER)
    @GetMapping("getById")
    public Result getById(@RequestParam("id")String id) {
        Param param = paramService.getById(id);
        return Result.success(param);
    }

    /** 保存参数 */
    @Log(name = "保存参数", type = BusinessType.INSERT)
    @PostMapping("saveParam")
    public Result saveParam(@RequestBody Param param) {
        QueryWrapper<Param> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Param::getParamKey, param.getParamKey());
        int count = paramService.count(queryWrapper);
        if (count > 0) {
            return Result.fail("参数键已存在！");
        }
        boolean save = paramService.save(param);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 修改参数 */
    @Log(name = "修改参数", type = BusinessType.UPDATE)
    @PostMapping("editParam")
    public Result editParam(@RequestBody Param apeParam) {
        Param param = paramService.getById(apeParam.getId());
        if (!param.getParamKey().equals(apeParam.getParamKey())) {
            QueryWrapper<Param> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Param::getParamKey,apeParam.getParamKey());
            int count = paramService.count(queryWrapper);
            if (count > 0) {
                return Result.fail("参数键已存在！");
            }
        }
        boolean update = paramService.updateById(apeParam);
        if (update) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除参数 */
    @Log(name = "删除参数", type = BusinessType.DELETE)
    @Transactional(rollbackFor = Exception.class)
    @GetMapping("removeParam")
    public Result removeParam(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                paramService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("参数id不能为空！");
        }
    }
}
