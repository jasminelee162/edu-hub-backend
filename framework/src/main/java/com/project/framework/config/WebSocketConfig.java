package com.project.framework.config;



import com.project.framework.server.DocWebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.context.ApplicationContext;

/**
 * WebSocket配置类，启用WebSocket支持并配置跨域
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer, ApplicationContextAware {



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        // 关键：将 ApplicationContext 注入到 DocWebSocketServer
        DocWebSocketServer.setApplicationContext(applicationContext);
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册WebSocket处理器并配置跨域
        registry.addHandler(myHandler(), "/api/websocket/sharedText/{sid}")
                .setAllowedOrigins("*") // 允许所有域名进行跨域调用
                .withSockJS(); // 启用SockJS后备选项，提高兼容性
    }

    @Bean
    public WebSocketHandler myHandler() {
        // 如果使用@ServerEndpoint注解，可以返回一个空实现
        // 实际处理逻辑在@ServerEndpoint注解的类中
        return new TextWebSocketHandler() {};
    }

    // 用于扫描和注册@ServerEndpoint注解的端点
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public CorsFilter webSocketCorsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/websocket/**", config); // 仅对WebSocket路径生效

        return new CorsFilter(source);
    }
}