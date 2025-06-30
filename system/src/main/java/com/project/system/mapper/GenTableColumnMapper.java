package com.project.system.mapper;

import com.project.system.domain.GenTableColumn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 代码生成字段mapper
 * @date 2023/10/10 9:26
 */
public interface GenTableColumnMapper extends BaseMapper<GenTableColumn> {

    /**
     * @description: 获取表字段
     * @param: table
     * @return:
     * @author shaozhujie
     * @date: 2023/10/11 9:45
     */
    List<Map<String, Object>> getTableField(@Param("table") String table);
}
