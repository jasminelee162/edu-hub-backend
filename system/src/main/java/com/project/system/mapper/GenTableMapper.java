package com.project.system.mapper;

import com.project.system.domain.GenTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 *
 * @version 1.0
 * @description: 代码生成mapper
 *
 */
public interface GenTableMapper extends BaseMapper<GenTable> {
    
    /**
    * @description: 获取数据库表
    * @param:
    * @return: 
    *
    *
    */
    Page<Map<String, Object>> getTables(Page<Map<String, Object>> page, @Param("ew") GenTable genTable, @Param("tables") List<String> tables);

    /**
     * @description: 获取数据库表详情
     * @param:
     * @return:
     *
     *
     */
    Map<String, Object> getTablesInfo(@Param("tableName") String table);
}
