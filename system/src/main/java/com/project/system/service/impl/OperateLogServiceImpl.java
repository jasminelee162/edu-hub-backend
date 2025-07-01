package com.project.system.service.impl;

import com.project.system.domain.OperateLog;
import com.project.system.mapper.OperateLogMapper;
import com.project.system.service.OperateLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 操作日志service实现类
 * @date 2023/9/22 9:55
 */
@Service
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog> implements OperateLogService {
}
