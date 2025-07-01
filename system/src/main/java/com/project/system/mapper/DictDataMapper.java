package com.project.system.mapper;

import com.project.system.domain.DictData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 *
 * @version 1.0
 * @description: 字典mapper
 * 
 */
public interface DictDataMapper extends BaseMapper<DictData> {
    //7.1 bug修改
    List<DictData> selectDistinctDictTypes();
}
