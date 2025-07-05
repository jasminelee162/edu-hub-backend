package com.project.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpAttributes;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.security.Principal;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 自定义 Principal，用于存储 userId
    public static class StompPrincipal implements Principal {
        private final String userId;

        public StompPrincipal(String userId) {
            this.userId = userId;
        }

        @Override
        public String getName() {
            return userId;
        }
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-doc")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    // 修改后的拦截器，处理所有STOMP命令，而非仅CONNECT
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor != null && accessor.getCommand() != null) {
                    // 对于CONNECT命令，从请求头获取userId并设置Principal
                    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                        String userId = accessor.getFirstNativeHeader("userId");
                        if (userId != null) {
                            // 创建并设置Principal
                            accessor.setUser(new StompPrincipal(userId));
                            System.out.println("WebSocket 认证成功，userId: " + userId);


                        } else {
                            System.out.println("WebSocket 连接缺少 userId");
                        }
                    }

                    // 对于非CONNECT命令，检查并恢复Principal（如果丢失）
                    else if (accessor.getUser() == null) {
                        // 尝试从会话属性恢复用户信息


                    }
                }

                return message;
            }
        });
    }

    // 添加会话属性持有者（如果没有的话）
    public static class SimpAttributesContextHolder {
        private static final ThreadLocal<SimpAttributes> attributesHolder = new ThreadLocal<>();

        public static void setAttributes(SimpAttributes attributes) {
            attributesHolder.set(attributes);
        }



        public static void resetAttributes() {
            attributesHolder.remove();
        }
    }
}