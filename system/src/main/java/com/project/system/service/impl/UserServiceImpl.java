package com.project.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.project.system.domain.TaskStudent;
import com.project.system.domain.User;
import com.project.system.mapper.UserMapper;
import com.project.system.service.EmailService;
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
    @Autowired
    private EmailService emailService;
    /**
     * 分页查询用户
     */

    private static String reject="您好，非常感谢您对的关注和应聘。经过综合评估，目前我们认为其他候选人更符合该岗位的需求。再次感谢您的时间和信任，我们会将您的简历存入人才库，未来有合适岗位时会优先联系。祝您求职顺利！";
    private static String accept="恭喜您！我们已经批准了您的入职申请，后续会把具体的入职时间和需要准备的材料发给您，欢迎加入咱们团队！";

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
            if(user.getChecked()==1&&user.getUserType()==1){
                list.add(user.getUserName());
            }
        }
        return list;
    }

    public void checked(String userName){
        User updateEntity = new User();
        updateEntity.setChecked(0);
        LambdaUpdateWrapper<User> userWrapper = new LambdaUpdateWrapper<>();
        userWrapper
                .eq(User::getUserName, userName);
        userMapper.update(updateEntity, userWrapper);

    }

    public void sendEmail(String userName){
        LambdaUpdateWrapper<User> userWrapper = new LambdaUpdateWrapper<>();
        userWrapper
                .eq(User::getUserName, userName);
        User user=userMapper.selectOne( userWrapper);
        if(user.getStatus()==1){
            emailService.sendEmail(user.getEmail(),reject);

        }
        else {
            emailService.sendEmail(user.getEmail(),accept);
        }
    }


}
