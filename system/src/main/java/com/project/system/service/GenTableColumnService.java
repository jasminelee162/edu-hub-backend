package com.project.system.service;

import com.project.system.domain.GenTableColumn;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 代码生成字段service
 * @date 2023/10/10 9:28
 */
public interface GenTableColumnService extends IService<GenTableColumn> {

    /**
    * @description: 获取表字段
    * @param: table
    * @return:
    * @author shaozhujie
    * @date: 2023/10/11 9:45
    */
    List<Map<String, Object>> getTableField(String table);
}
