package com.project.system.mapper;

import com.project.system.domain.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 *
 * @version 1.0
 * @description: 角色菜单关系mapper
 * 
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
    * @description: 根据角色获取权限
    * @param: role
    * @return:
    *
    */
    Set<String> getRoleMenusSet(@Param("role") String role);
}
