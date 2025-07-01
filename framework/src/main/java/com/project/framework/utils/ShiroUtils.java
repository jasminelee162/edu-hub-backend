package com.project.framework.utils;

import com.project.system.domain.User;
import org.apache.shiro.SecurityUtils;

/**
 *
 * @version 1.0
 * @description: shiro工具类
 *
 */
public class ShiroUtils {

    /**
    * @description: 获取当前登陆用户
    * @param:
    * @return:
    *
    *
    */
    public static User getUserInfo(){
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

}
