package com.project.system.service.impl;

import com.project.system.domain.User;
import com.project.system.mapper.UserMapper;
import com.project.system.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @description: 用户service实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 分页查询用户
     */
    @Override
    public Page<User> getUserPage(User user) {
        Page<User> page = new Page<>(user.getPageNumber(), user.getPageSize());
        return baseMapper.getUserPage(page, user);
    }
}
