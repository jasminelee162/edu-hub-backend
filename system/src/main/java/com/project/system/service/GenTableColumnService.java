package com.project.system.service;

import com.project.system.domain.GenTableColumn;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * @version 1.0
 * @description: 代码生成字段service
 *
 */
public interface GenTableColumnService extends IService<GenTableColumn> {

    /**
    * @description: 获取表字段
    * @param: table
    * @return:
    *
    */
    List<Map<String, Object>> getTableField(String table);
}
