package com.project.system.service;

import com.project.system.domain.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 用户角色关系service
 * @date 2023/8/31 14:36
 */
public interface UserRoleService extends IService<UserRole> {

    /**
    * @description: 根据账号获取角色
    * @param: loginAccount
    * @return:
    * @author shaozhujie
    * 
    */
    Set<String> getUserRolesSet(String loginAccount);

}
