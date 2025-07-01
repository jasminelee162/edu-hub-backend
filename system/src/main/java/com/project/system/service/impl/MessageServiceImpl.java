package com.project.system.service.impl;

import com.project.system.domain.Message;
import com.project.system.mapper.MessageMapper;
import com.project.system.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 * @version 1.0
 * @description: 留言表service实现类
 * 
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
}