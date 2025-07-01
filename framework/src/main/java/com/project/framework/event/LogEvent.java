package com.project.framework.event;

import com.project.system.domain.OperateLog;
import org.springframework.context.ApplicationEvent;

/**
 *  
 * @version 1.0
 * @description: 操作日志监听类
 *  2023/9/22 10:57
 */
public class LogEvent extends ApplicationEvent {

    private OperateLog source;

    public LogEvent(OperateLog source) {
        super(source);
        this.source = source;
    }

    @Override
    public OperateLog getSource() {
        return source;
    }

    public void setSource(OperateLog source) {
        this.source = source;
    }
}
