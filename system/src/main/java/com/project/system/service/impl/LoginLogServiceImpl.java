package com.project.system.service.impl;

import com.project.system.domain.LoginLog;
import com.project.system.mapper.LoginLogMapper;
import com.project.system.service.LoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 登陆日志service实现类
 * @date 2023/9/23 8:52
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

}
