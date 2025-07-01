package com.project.system.mapper;

import com.project.system.domain.GenTableColumn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * @version 1.0
 * @description: 代码生成字段mapper
 *
 */
public interface GenTableColumnMapper extends BaseMapper<GenTableColumn> {

    /**
     * @description: 获取表字段
     * @param: table
     * @return:
     */
    List<Map<String, Object>> getTableField(@Param("table") String table);
}
