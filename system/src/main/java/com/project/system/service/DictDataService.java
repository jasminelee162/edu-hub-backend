package com.project.system.service;

import com.project.system.domain.DictData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 
 * @version 1.0
 * @description: 字典service
 * @date 2023/10/9 14:30
 */
public interface DictDataService extends IService<DictData> {

    List<DictData> getDistinctDictTypes();
}
