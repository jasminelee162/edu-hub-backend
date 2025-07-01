package com.ape.apeframework.event;

import com.ape.apesystem.domain.ApeOperateLog;
import org.springframework.context.ApplicationEvent;

/**
 *  
 * @version 1.0
 * @description: 操作日志监听类
 * 
 */
public class LogEvent extends ApplicationEvent {

    private ApeOperateLog source;

    public LogEvent(ApeOperateLog source) {
        super(source);
        this.source = source;
    }

    @Override
    public ApeOperateLog getSource() {
        return source;
    }

    public void setSource(ApeOperateLog source) {
        this.source = source;
    }
}
