package com.project.system.service.impl;

import com.project.system.domain.GenTable;
import com.project.system.mapper.GenTableMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.system.service.GenTableService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @description: 代码生成service实现类
 */
@Service
public class GenTableServiceImpl extends ServiceImpl<GenTableMapper, GenTable> implements GenTableService {

    /**
    * 获取数据库表
    */
    /*@Override
    public Page<Map<String, Object>> getTables(GenTable genTable) {
        Page<Map<String, Object>> page = new Page<>(genTable.getPageNumber(), genTable.getPageSize());
        QueryWrapper<GenTable> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().groupBy(GenTable::getTableName);
        List<GenTable> genTables = baseMapper.selectList(queryWrapper);
        List<String> tables = new ArrayList<>();
        genTables.forEach(item -> {
            tables.add(item.getTableName());
        });
        return baseMapper.getTables(page, genTable,tables);
    }*/

    /* 7.1 bug修改*/
    @Override
    public Page<Map<String, Object>> getTables(GenTable genTable) {
        Page<Map<String, Object>> page = new Page<>(genTable.getPageNumber(), genTable.getPageSize());
        QueryWrapper<GenTable> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT table_name"); // 只查询唯一的table_name
        List<GenTable> genTables = baseMapper.selectList(queryWrapper);
        List<String> tables = genTables.stream()
                .map(GenTable::getTableName)
                .collect(Collectors.toList());
        return baseMapper.getTables(page, genTable, tables);
    }

    /**
     * 获取数据库表详情
     */
    @Override
    public Map<String, Object> getTablesInfo(String table) {
        Map<String, Object> tablesInfo = baseMapper.getTablesInfo(table);
        if (tablesInfo == null) {
            throw new RuntimeException("表" + table + "不存在于数据库中");
        }
        return tablesInfo;
    }
}
