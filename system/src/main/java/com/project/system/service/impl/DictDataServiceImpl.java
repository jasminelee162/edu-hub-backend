package com.project.system.service.impl;

import com.project.system.domain.DictData;
import com.project.system.mapper.DictDataMapper;
import com.project.system.service.DictDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 字典service实现类
 * @date 2023/10/9 14:31
 */
@Service
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {
}
