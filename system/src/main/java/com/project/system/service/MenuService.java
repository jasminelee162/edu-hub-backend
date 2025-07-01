package com.project.system.service;

import com.project.system.domain.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 * @version 1.0
 * @description: 菜单service
 *
 */
public interface MenuService extends IService<Menu> {

    /**
    * @description: 根据用户获取菜单权限
    * @param: id
    * @return:
    *
    *  2023/9/13 9:39
    */
    List<Menu> getMenuByUser(String id);

}
