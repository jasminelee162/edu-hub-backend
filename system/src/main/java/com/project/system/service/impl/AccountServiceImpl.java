package com.project.system.service.impl;

import com.project.system.domain.Account;
import com.project.system.mapper.AccountMapper;
import com.project.system.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 公告service实现类
 * @date 2023/9/21 8:47
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
}
