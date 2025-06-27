package com.ape.apeframework.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
//import sun.plugin2.util.SystemUtil;

/**
 * @author jas
 * @version 1.0
 * @description: 项目整体配置信息
 * @date 2025/6/26
 */
@Component
@ConfigurationProperties(prefix = "system")
public class SystemConfig {

    /**
     * 网络访问日志开关
     */
    private static boolean requestLog;

    public static boolean isRequestLog() {
        return requestLog;
    }

    public void setRequestLog(boolean requestLog) {
        SystemConfig.requestLog = requestLog;
    }
}