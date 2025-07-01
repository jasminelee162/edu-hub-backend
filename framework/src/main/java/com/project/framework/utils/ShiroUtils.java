package com.project.framework.utils;

import com.project.system.domain.User;
import org.apache.shiro.SecurityUtils;

/**
 *  shaozhujie
 * @version 1.0
 * @description: shiro工具类
 * @date 2023/9/12 10:52
 */
public class ShiroUtils {

    /**
    * @description: 获取当前登陆用户
    * @param:
    * @return:
    *  shaozhujie
    * @date: 2023/9/12 10:54
    */
    public static User getUserInfo(){
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

}
