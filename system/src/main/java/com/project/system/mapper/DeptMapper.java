package com.project.system.mapper;

import com.project.system.domain.Dept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @author 
 * @version 1.0
 * @description: 部门mapper
 * @date 2023/8/28 10:11
 */
public interface DeptMapper extends BaseMapper<Dept> {

    /**
    * @description: 查询下拉列表
    * @return: List
    * @author 
    * @date: 2023/8/29 10:43
    */
    List<Map<String, Object>> getDeptDrop();

    /**
     * @description: 查询一级部门
     * @return: List
     * @author 
     * @date: 2023/8/29 10:43
     */
    List<Map<String, Object>> getDeptDropFirst();
}
