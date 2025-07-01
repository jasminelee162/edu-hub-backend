package com.project.system.service.impl;

import com.project.system.domain.Menu;
import com.project.system.mapper.MenuMapper;
import com.project.system.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @version 1.0
 * @description: 菜单service实现类
 *
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    /**
    * 根据用户获取菜单权限
    */
    @Override
    public List<Menu> getMenuByUser(String id) {
        return baseMapper.getMenuByUser(id);
    }
}
