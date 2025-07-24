package com.project.framework.utils;

import com.project.system.domain.User;
import org.apache.shiro.SecurityUtils;


public class ShiroUtils {

    /**
    * @description: 获取当前登录用户
    * @param:
    * @return:
    * @author shaozhujie
    * 
    */
    public static User getUserInfo(){
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

}
