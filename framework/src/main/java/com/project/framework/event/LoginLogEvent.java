package com.project.framework.event;

import com.project.system.domain.LoginLog;
import org.springframework.context.ApplicationEvent;

/**
 *  
 * @version 1.0
 * @description: 登陆日志监听类
 *
 */
public class LoginLogEvent extends ApplicationEvent {

    private LoginLog source;

    public LoginLogEvent(LoginLog source) {
        super(source);
        this.source = source;
    }

    @Override
    public LoginLog getSource() {
        return source;
    }

    public void setSource(LoginLog source) {
        this.source = source;
    }
}
