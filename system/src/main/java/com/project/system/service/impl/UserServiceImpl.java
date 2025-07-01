package com.project.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.system.domain.TaskStudent;
import com.project.system.domain.User;
import com.project.system.mapper.UserMapper;
import com.project.system.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @description: 用户service实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    private UserMapper userMapper;
    /**
     * 分页查询用户
     */
    @Override
    public Page<User> getUserPage(User user) {
        Page<User> page = new Page<>(user.getPageNumber(), user.getPageSize());
        return baseMapper.getUserPage(page, user);
    }

    //获取是否有未审核的入驻教师申请
    public List<String> unRead(){
        List<String> list = new ArrayList<>();
        List<User> userList=userMapper.selectList(null);
        for(User user:userList){
            if(user.getDelFlag()==1){
                list.add(user.getUserName());
            }
        }
        return list;
    }
}
