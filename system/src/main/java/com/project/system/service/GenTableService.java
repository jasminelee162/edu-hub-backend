package com.project.system.service;

import com.project.system.domain.GenTable;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 *
 * @version 1.0
 * @description: 代码生成service
 */
public interface GenTableService extends IService<GenTable> {

    /**
    * @description: 获取数据库表
    * @param:
    * @return:
    */
    Page<Map<String,Object>> getTables(GenTable genTable);

    /**
     * @description: 获取数据库表详情
     * @param:
     * @return:
     *
     */
    Map<String, Object> getTablesInfo(String table);
}
