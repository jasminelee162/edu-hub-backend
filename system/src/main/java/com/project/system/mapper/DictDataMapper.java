package com.project.system.mapper;

import com.project.system.domain.DictData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 字典mapper
 * @date 2023/10/9 14:29
 */
public interface DictDataMapper extends BaseMapper<DictData> {
    //7.1 bug修改
    List<DictData> selectDistinctDictTypes();
}
