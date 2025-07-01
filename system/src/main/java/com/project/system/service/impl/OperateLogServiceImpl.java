package com.project.system.service.impl;

import com.project.system.domain.OperateLog;
import com.project.system.mapper.OperateLogMapper;
import com.project.system.service.OperateLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 * @version 1.0
 * @description: 操作日志service实现类
 *
 */
@Service
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog> implements OperateLogService {
}
