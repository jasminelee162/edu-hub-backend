package com.project.system.service;

import com.project.system.domain.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 * @version 1.0
 * @description: 用户service
 *
 */
public interface UserService extends IService<User> {

    /**
    * @description: 分页查询用户
    * @param: user
    * @return: Page
    *
    */
    Page<User> getUserPage(User user);

}
