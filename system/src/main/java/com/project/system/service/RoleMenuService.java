package com.project.system.service;

import com.project.system.domain.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 *
 * @version 1.0
 * @description: 角色菜单关系service
 * 
 */
public interface RoleMenuService extends IService<RoleMenu> {

    /**
     * @description: 根据角色获取权限
     * @param: loginAccount
     * @return:
     *
     *
     */
    Set<String> getRoleMenusSet(String role);
}
