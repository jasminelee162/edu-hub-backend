package com.project.system.service;

import com.project.system.domain.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 菜单service
 * @date 2023/8/30 9:23
 */
public interface MenuService extends IService<Menu> {

    /**
    * @description: 根据用户获取菜单权限
    * @param: id
    * @return:
    * @author shaozhujie
    * 
    */
    List<Menu> getMenuByUser(String id);

}
