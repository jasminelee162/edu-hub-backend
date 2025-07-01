package com.project.system.service.impl;

import com.project.system.domain.RoleMenu;
import com.project.system.mapper.RoleMenuMapper;
import com.project.system.service.RoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 *
 * @version 1.0
 * @description: 角色菜单关系service实现类
 * 
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    /**
    *  根据角色获取权限
    */
    @Override
    public Set<String> getRoleMenusSet(String role) {
        return baseMapper.getRoleMenusSet(role);
    }
}
