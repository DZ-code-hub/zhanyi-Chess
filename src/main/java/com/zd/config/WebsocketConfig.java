package com.zd.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                        // 优先使用前端传入的 clientId 保持 WS 与 HTTP 的同一用户ID
                        String query = request.getURI().getQuery();
                        if (query != null) {
                            for (String part : query.split("&")) {
                                String[] kv = part.split("=", 2);
                                if (kv.length == 2 && kv[0].equals("clientId") && !kv[1].isEmpty()) {
                                    final String name = kv[1];
                                    log.info("name:{}",name);
                                    return new Principal() { @Override public String getName() {return name; } };
                                }
                            }
                        }
                        String cookies = request.getHeaders().getFirst("Cookie");
                        String jsid = null;
                        if (cookies != null) {
                            for (String part : cookies.split(";")) {
                                String p = part.trim();
                                if (p.startsWith("JSESSIONID=")) {
                                    jsid = p.substring("JSESSIONID=".length());
                                    break;
                                }
                            }
                        }
                        final String name = jsid != null ? ("http-" + jsid) : ("u-" + UUID.randomUUID());
                        return new Principal() { @Override public String getName() { return name; } };
                    }
                })
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }
    //消息转化器配置
    //用于当客户端和服务器通过websocket传输消息时，可以将Java对象序列化为json字符串或将json字符串反序列化为Java对象
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        // 添加字符串消息转换器，确保UTF-8编码
        messageConverters.add(new StringMessageConverter());
        // 添加JSON消息转换器
        messageConverters.add(new MappingJackson2MessageConverter());
        return false;
    }

}
