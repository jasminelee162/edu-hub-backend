package com.project.system.service;

import com.project.system.domain.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * @author 
 * @version 1.0
 * @description: 角色菜单关系service
 * @date 2023/8/31 10:57
 */
public interface RoleMenuService extends IService<RoleMenu> {

    /**
     * @description: 根据角色获取权限
     * @param: loginAccount
     * @return:
     * @author 
     * @date: 2023/9/7 17:01
     */
    Set<String> getRoleMenusSet(String role);
}
