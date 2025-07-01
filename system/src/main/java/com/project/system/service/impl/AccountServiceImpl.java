package com.project.system.service.impl;

import com.project.system.domain.Account;
import com.project.system.mapper.AccountMapper;
import com.project.system.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 * @version 1.0
 * @description: 公告service实现类
 *
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
}
