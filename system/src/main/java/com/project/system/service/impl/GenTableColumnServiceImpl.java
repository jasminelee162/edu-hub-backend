package com.project.system.service.impl;

import com.project.system.domain.GenTableColumn;
import com.project.system.mapper.GenTableColumnMapper;
import com.project.system.service.GenTableColumnService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 代码生成字段service实现类
 * @date 2023/10/10 9:28
 */
@Service
public class GenTableColumnServiceImpl extends ServiceImpl<GenTableColumnMapper, GenTableColumn> implements GenTableColumnService {

    /**
    * 获取表字段
    */
    @Override
    public List<Map<String, Object>> getTableField(String table) {
        return baseMapper.getTableField(table);
    }
}
