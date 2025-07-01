package com.project.system.mapper;

import com.project.system.domain.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 
 * @version 1.0
 * @description: 菜单mapper
 * @date 2023/8/30 9:22
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
    * @description: 根据用户获取菜单权限
    * @param: id
    * @return:
    * @author 
    * @date: 2023/9/13 9:39
    */
    List<Menu> getMenuByUser(@Param("id") String id);

}
