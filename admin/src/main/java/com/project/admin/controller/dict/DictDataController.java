package com.project.admin.controller.dict;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.common.utils.StringUtils;
import com.project.system.domain.DictData;
import com.project.system.service.DictDataService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 字典controller
 * @date 2023/10/9 14:32
 */
@Controller
@ResponseBody
@RequestMapping("dict")
public class DictDataController {

    @Autowired
    private DictDataService dictDataService;

    /**
     * 查询
     */
    @Log(name = "查询字典列表", type = BusinessType.OTHER)
    @PostMapping("getDictPage")
    public Result getDictPage(@RequestBody DictData dictData) {
        Page<DictData> page = new Page<>(dictData.getPageNumber(), dictData.getPageSize());
        QueryWrapper<DictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(dictData.getDictCode()), DictData::getDictCode, dictData.getDictCode())
                .like(StringUtils.isNotBlank(dictData.getDictLabel()), DictData::getDictLabel, dictData.getDictLabel())
                .eq(dictData.getStatus() != null, DictData::getStatus, dictData.getStatus())
                .orderByDesc(DictData::getDictCode).orderByDesc(DictData::getCreateTime);
        Page<DictData> dictDataPage = dictDataService.page(page, queryWrapper);
        return Result.success(dictDataPage);
    }

    /**
     * 查询
     */
    /*@Log(name = "查询全部字典类型", type = BusinessType.OTHER)
    @GetMapping("getDictTypeList")
    public Result getDictTypeList() {
        QueryWrapper<DictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().groupBy(DictData::getDictCode);
        List<DictData> dataList = dictDataService.list(queryWrapper);
        return Result.success(dataList);
    }*/
    //7.1 bug修改
    @Log(name = "查询全部字典类型", type = BusinessType.OTHER)
    @GetMapping("getDictTypeList")
    public Result getDictTypeList() {
        List<DictData> dataList = dictDataService.getDistinctDictTypes();
        return Result.success(dataList);
    }

    /** 根据id获取字典 */
    @Log(name = "根据id获取字典", type = BusinessType.OTHER)
    @GetMapping("getDictById")
    public Result getDictById(@RequestParam("id")String id) {
        DictData dictData = dictDataService.getById(id);
        return Result.success(dictData);
    }

    /** 保存字典 */
    @Log(name = "保存字典", type = BusinessType.INSERT)
    @PostMapping("saveDict")
    public Result saveDict(@RequestBody DictData dictData) {
        boolean save = dictDataService.save(dictData);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑 */
    @Log(name = "编辑字典", type = BusinessType.UPDATE)
    @PostMapping("editDict")
    public Result editDict(@RequestBody DictData dictData) {
        boolean save = dictDataService.updateById(dictData);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除字典 */
    @GetMapping("removeDict")
    @Log(name = "删除字典", type = BusinessType.DELETE)
    public Result removeDict(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                dictDataService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("字典id不能为空！");
        }
    }

    /** 根据编码获取字典数据 */
    @GetMapping("getDictByCode")
    @Log(name = "根据编码获取字典数据", type = BusinessType.OTHER)
    public Result getDictByCode(@RequestParam("code")String code) {
        QueryWrapper<DictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DictData::getDictCode,code).orderByAsc(DictData::getDictSort);
        List<DictData> dictDataList = dictDataService.list(queryWrapper);
        return Result.success(dictDataList);
    }

    /** 根据编码获取字典数据和标签获取值 */
    @GetMapping("getDictByCodeAndLabel")
    @Log(name = "根据编码获取字典数据和标签获取值", type = BusinessType.OTHER)
    public Result getDictByCodeAndLabel(@RequestParam("code")String code,@RequestParam("label")String label) {
        QueryWrapper<DictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DictData::getDictCode,code)
                .eq(DictData::getDictLabel,label).last("limit 1");
        DictData dictData = dictDataService.getOne(queryWrapper);
        return Result.success(dictData);
    }

}
