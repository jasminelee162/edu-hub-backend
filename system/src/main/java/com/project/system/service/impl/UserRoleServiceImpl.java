package com.project.system.service.impl;

import com.project.system.domain.UserRole;
import com.project.system.mapper.UserRoleMapper;
import com.project.system.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author 
 * @version 1.0
 * @description: 用户角色关系service实现类
 * @date 2023/8/31 14:37
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    /**
     * 根据账号获取角色
     */
    @Override
    public Set<String> getUserRolesSet(String loginAccount) {
        return baseMapper.getUserRolesSet(loginAccount);
    }

}
