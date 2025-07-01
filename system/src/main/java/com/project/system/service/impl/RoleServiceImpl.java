package com.project.system.service.impl;

import com.project.system.domain.Role;
import com.project.system.mapper.RoleMapper;
import com.project.system.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 * @version 1.0
 * @description: 角色service实现类
 * 
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
