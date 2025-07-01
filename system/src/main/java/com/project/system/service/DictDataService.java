package com.project.system.service;

import com.project.system.domain.DictData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 * @version 1.0
 * @description: 字典service
 * 
 */
public interface DictDataService extends IService<DictData> {

    List<DictData> getDistinctDictTypes();
}
