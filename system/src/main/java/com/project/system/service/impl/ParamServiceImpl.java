package com.project.system.service.impl;

import com.project.system.domain.Param;
import com.project.system.mapper.ParamMapper;
import com.project.system.service.ParamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 
 * @version 1.0
 * @description: 参数service实现类
 * @date 2023/9/20 16:51
 */
@Service
public class ParamServiceImpl extends ServiceImpl<ParamMapper, Param> implements ParamService {
}
