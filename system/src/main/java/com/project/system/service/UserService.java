package com.project.system.service;

import com.project.system.domain.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 用户service
 * @date 2023/8/28 8:45
 */
public interface UserService extends IService<User> {

    /**
    * @description: 分页查询用户
    * @param: user
    * @return: Page
    * @author shaozhujie
    * 
    */
    Page<User> getUserPage(User user);
    List<String> unRead();
    void checked(String userName);
    void sendEmail(String userName);

}
