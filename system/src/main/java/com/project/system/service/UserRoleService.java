package com.project.system.service;

import com.project.system.domain.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 *
 * @version 1.0
 * @description: 用户角色关系service
 * 
 */
public interface UserRoleService extends IService<UserRole> {

    /**
    * @description: 根据账号获取角色
    * @param: loginAccount
    * @return:
    */
    Set<String> getUserRolesSet(String loginAccount);

}
