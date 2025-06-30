package com.project.system.mapper;

import com.project.system.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 用户mapper
 * @date 2023/8/28 8:41
 */
public interface UserMapper extends BaseMapper<User> {

    /**
    * 分页查询用户
    */
    Page<User> getUserPage(Page<User> page, @Param("ew") User user);

}
