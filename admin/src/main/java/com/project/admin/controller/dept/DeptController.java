package com.project.admin.controller.dept;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.system.domain.Dept;
import com.project.system.service.DeptService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 部门controller
 * @date 2023/8/28 11:37
 */
@Controller
@ResponseBody
@RequestMapping("dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    /** 查询 */
    @Log(name = "查询部门列表", type = BusinessType.OTHER)
    @PostMapping("getDeptList")
    public Result getDeptPage(@RequestBody Dept apeDept) {
        //构造查询条件
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(apeDept.getDeptName()), Dept::getDeptName,apeDept.getDeptName())
                .eq(apeDept.getStatus() != null, Dept::getStatus,apeDept.getStatus()).orderByAsc(Dept::getOrderNum);
        //查询
        List<Dept> depts = deptService.list(queryWrapper);
        //筛选出第一级
        List<Dept> first = depts.stream().filter(item -> "0".equals(item.getParentId())).collect(Collectors.toList());
        if (first.size() <= 0) {
            return Result.success(depts);
        } else {
            for (Dept dept : first) {
                filterDept(dept, depts);
            }
            return Result.success(first);
        }
    }

    /** 递归查询下级部门 */
    public void filterDept(Dept apeDept, List<Dept> apeDepts) {
        List<Dept> depts = new ArrayList<>();
        for (Dept dept : apeDepts) {
            if (apeDept.getId().equals(dept.getParentId())) {
                depts.add(dept);
                filterDept(dept,apeDepts);
            }
        }
        apeDept.setChildren(depts);
    }

    /** 根据id查询部门 */
    @Log(name = "根据id查询部门", type = BusinessType.OTHER)
    @GetMapping("getById")
    public Result getById(@RequestParam("id") String id) {
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }

    /** 保存 */
    @Log(name = "保存部门", type = BusinessType.INSERT)
    @PostMapping("saveDept")
    public Result saveDept(@RequestBody Dept dept) {
        boolean save = deptService.save(dept);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑 */
    @Log(name = "编辑部门", type = BusinessType.UPDATE)
    @PostMapping("editDept")
    public Result editDept(@RequestBody Dept dept){
        boolean edit = deptService.updateById(dept);
        if (edit) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除 */
    @Log(name = "删除部门", type = BusinessType.DELETE)
    @GetMapping("removeDept")
    public Result removeDept(@RequestParam("id")String id) {
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Dept::getParentId,id);
        int count = deptService.count(queryWrapper);
        if (count > 0) {
            return Result.fail("存在下级部门,请先删除下级部门！");
        }
        boolean remove = deptService.removeById(id);
        if (remove) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

}
