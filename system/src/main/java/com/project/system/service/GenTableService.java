package com.project.system.service;

import com.project.system.domain.GenTable;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 代码生成service
 * @date 2023/10/10 9:23
 */
public interface GenTableService extends IService<GenTable> {

    /**
    * @description: 获取数据库表
    * @param:
    * @return:
    * @author shaozhujie
    * 
    */
    Page<Map<String,Object>> getTables(GenTable genTable);

    /**
     * @description: 获取数据库表详情
     * @param:
     * @return:
     * @author shaozhujie
     * 
     */
    Map<String, Object> getTablesInfo(String table);
}
