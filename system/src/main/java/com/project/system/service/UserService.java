package com.project.system.service;

import com.project.system.domain.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 
 * @version 1.0
 * @description: 用户service
 * @date 2023/8/28 8:45
 */
public interface UserService extends IService<User> {

    /**
    * @description: 分页查询用户
    * @param: user
    * @return: Page
    * @author 
    * @date: 2023/8/28 10:49
    */
    Page<User> getUserPage(User user);

}
