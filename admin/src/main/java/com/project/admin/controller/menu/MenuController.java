package com.project.admin.controller.menu;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.framework.utils.ShiroUtils;
import com.project.system.domain.Menu;
import com.project.system.domain.RoleMenu;
import com.project.system.domain.User;
import com.project.system.service.MenuService;
import com.project.system.service.RoleMenuService;
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
 * @description: 菜单controller
 * @date 2023/8/30 9:25
 */
@Controller
@ResponseBody
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleMenuService roleMenuService;

    /** 获取菜单列表 */
    @Log(name = "获取菜单列表", type = BusinessType.OTHER)
    @PostMapping("getMenuList")
    public Result getMenuList(@RequestBody Menu apeMenu) {
        //构造查询条件
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(apeMenu.getMenuName()), Menu::getMenuName,apeMenu.getMenuName())
                .eq(apeMenu.getStatus() != null, Menu::getStatus,apeMenu.getStatus()).orderByAsc(Menu::getOrderNum);
        //查询
        List<Menu> menus = menuService.list(queryWrapper);
        //筛选出第一级
        List<Menu> first = menus.stream().filter(item -> "0".equals(item.getParentId())).collect(Collectors.toList());
        if (first.size() <= 0) {
            return Result.success(menus);
        } else {
            for (Menu menu : first) {
                filterMenu(menu, menus);
            }
            return Result.success(first);
        }
    }

    /** 递归查询下级菜单 */
    public void filterMenu(Menu apeMenu, List<Menu> apeMenus) {
        List<Menu> menus = new ArrayList<>();
        for (Menu menu : apeMenus) {
            if (apeMenu.getId().equals(menu.getParentId())) {
                menus.add(menu);
                filterMenu(menu,apeMenus);
            }
        }
        apeMenu.setChildren(menus);
    }

    @Log(name = "根据id获取菜单", type = BusinessType.OTHER)
    @GetMapping("getById")
    public Result getById(@RequestParam("id")String id) {
        Menu menu = menuService.getById(id);
        return Result.success(menu);
    }

    /** 保存菜单 */
    @Log(name = "保存菜单保存菜单", type = BusinessType.INSERT)
    @PostMapping("saveMenu")
    public Result saveMenu(@RequestBody Menu menu) {
        if (menu.getMenuType() != 0 ) {
            QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Menu::getPerms, menu.getPerms());
            int count = menuService.count(queryWrapper);
            if (count > 0) {
                return Result.fail("权限字符已存在！");
            }
        }
        boolean save = menuService.save(menu);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑菜单 */
    @Log(name = "编辑菜单", type = BusinessType.UPDATE)
    @PostMapping("editMenu")
    public Result editMenu(@RequestBody Menu apeMenu) {
        if (StringUtils.isNotBlank(apeMenu.getIdArrary())) {
            apeMenu.setParentId("0");
        }
        Menu menu = menuService.getById(apeMenu.getId());
        if (apeMenu.getMenuType() != 0 ) {
            if (!menu.getPerms().equals(apeMenu.getPerms())) {
                QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(Menu::getPerms,apeMenu.getPerms());
                int count = menuService.count(queryWrapper);
                if (count > 0) {
                    return Result.fail("权限字符已存在！");
                }
            }
        }
        boolean update = menuService.updateById(apeMenu);
        if (update) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除菜单 */
    @Log(name = "删除菜单", type = BusinessType.DELETE)
    @GetMapping("removeMenu")
    public Result removeMenu(@RequestParam("id")String id) {
        //先查有没有分配给用户
        QueryWrapper<RoleMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RoleMenu::getMenuId,id);
        int num = roleMenuService.count(wrapper);
        if (num > 0) {
            return Result.fail("菜单已分配给角色，请先解除角色菜单！");
        }
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Menu::getParentId,id);
        int count = menuService.count(queryWrapper);
        if (count > 0) {
            return Result.fail("存在下级菜单,请先删除下级菜单！");
        }
        boolean remove = menuService.removeById(id);
        if (remove) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 根据用户获取菜单权限 */
    @Log(name = "根据用户获取菜单权限", type = BusinessType.OTHER)
    @GetMapping("getMenuByUser")
    public Result getMenuByUser() {
        User user = ShiroUtils.getUserInfo();
        List<Menu> menus = menuService.getMenuByUser(user.getId());
        return Result.success(menus);
    }

}
