package com.project.system.service.impl;

import com.project.system.domain.LoginLog;
import com.project.system.mapper.LoginLogMapper;
import com.project.system.service.LoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 * @version 1.0
 * @description: 登陆日志service实现类
 *
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

}
