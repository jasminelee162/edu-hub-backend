package com.project.system.mapper;

import com.project.system.domain.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 菜单mapper
 * @date 2023/8/30 9:22
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
    * @description: 根据用户获取菜单权限
    * @param: id
    * @return:
    * @author shaozhujie
    * 
    */
    List<Menu> getMenuByUser(@Param("id") String id);

}
