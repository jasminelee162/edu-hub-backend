package com.project.admin.controller.role;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.system.domain.Role;
import com.project.system.domain.RoleMenu;
import com.project.system.domain.UserRole;
import com.project.system.service.RoleMenuService;
import com.project.system.service.RoleService;
import com.project.system.service.UserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @description: TODO
 */
@Controller
@ResponseBody
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private UserRoleService userRoleService;

    /** 分页获取角色 */
    @Log(name = "分页获取角色", type = BusinessType.OTHER)
    @PostMapping("getRolePage")
    public Result getRolePage(@RequestBody Role role) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(role.getRoleName()), Role::getRoleName, role.getRoleName())
                .like(StringUtils.isNotBlank(role.getRoleKey()), Role::getRoleKey, role.getRoleKey())
                .eq(role.getStatus() != null, Role::getStatus, role.getStatus());
        Page<Role> page = new Page<>(role.getPageNumber(), role.getPageSize());
        Page<Role> rolePage = roleService.page(page, queryWrapper);
        return Result.success(rolePage);
    }

    /** 获取角色列表 */
    @Log(name = "获取角色列表", type = BusinessType.OTHER)
    @PostMapping("getRoleList")
    public Result getRoleList(@RequestBody Role role) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(role.getRoleName()), Role::getRoleName, role.getRoleName())
                .like(StringUtils.isNotBlank(role.getRoleKey()), Role::getRoleKey, role.getRoleKey())
                .eq(role.getStatus() != null, Role::getStatus, role.getStatus());
        List<Role> roleList = roleService.list(queryWrapper);
        return Result.success(roleList);
    }

    /** 根据id获取角色 */
    @Log(name = "根据id获取角色", type = BusinessType.OTHER)
    @GetMapping("getRoleById")
    public Result getRoleById(@RequestParam("id")String id) {
        Role role = roleService.getById(id);
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleMenu::getRoleId, role.getId());
        List<RoleMenu> roleMenus = roleMenuService.list(queryWrapper);
        List<String> menuIds = new ArrayList<>();
        for (RoleMenu roleMenu : roleMenus) {
            menuIds.add(roleMenu.getMenuId());
        }
        role.setMenuIds(menuIds);
        return Result.success(role);
    }

    /** 保存角色 */
    @Log(name = "保存角色", type = BusinessType.INSERT)
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("saveRole")
    public Result saveRole(@RequestBody Role role) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Role::getRoleKey, role.getRoleKey());
        int count = roleService.count(queryWrapper);
        if (count > 0) {
            return Result.fail("权限字符已存在！");
        }
        //先保存角色
        String uuid = IdWorker.get32UUID();
        role.setId(uuid);
        roleService.save(role);
        //再保存角色菜单关系
        List<String> menuIds = role.getMenuIds();
        List<RoleMenu> roleMenuList = new ArrayList<>();
        for (String menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(uuid);
            roleMenu.setMenuId(menuId);
            roleMenuList.add(roleMenu);
        }
        roleMenuService.saveBatch(roleMenuList);
        return Result.success();
    }

    /** 编辑角色 */
    @Log(name = "编辑角色", type = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("editRole")
    public Result editRole(@RequestBody Role apeRole) {
        Role role = roleService.getById(apeRole.getId());
        if (!role.getRoleKey().equals(apeRole.getRoleKey())) {
            QueryWrapper<Role> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(Role::getRoleKey,apeRole.getRoleKey());
            int count = roleService.count(wrapper);
            if (count > 0) {
                return Result.fail("权限字符已存在！");
            }
        }
        //更新角色
        roleService.updateById(apeRole);
        //把角色菜单关系删除
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleMenu::getRoleId,apeRole.getId());
        roleMenuService.remove(queryWrapper);
        //删除之后再重新保存
        List<String> menuIds = apeRole.getMenuIds();
        List<RoleMenu> roleMenuList = new ArrayList<>();
        for (String menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(apeRole.getId());
            roleMenu.setMenuId(menuId);
            roleMenuList.add(roleMenu);
        }
        roleMenuService.saveBatch(roleMenuList);
        return Result.success();
    }

    /** 删除角色 */
    @Log(name = "删除角色", type = BusinessType.DELETE)
    @Transactional(rollbackFor = Exception.class)
    @GetMapping("removeRole")
    public Result removeRole(@RequestParam("ids") String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                //先查有没有分配给用户
                QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(UserRole::getRoleId,id);
                int count = userRoleService.count(wrapper);
                if (count > 0) {
                    return Result.fail("角色已分配给用户，请先解除用户角色！");
                }
                //删除角色和菜单关系
                roleService.removeById(id);
                QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(RoleMenu::getRoleId,id);
                roleMenuService.remove(queryWrapper);
            }
            return Result.success();
        } else {
            return Result.fail("角色id不能为空！");
        }
    }

}
