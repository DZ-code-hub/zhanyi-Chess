package com.zd.HandshakeHander;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Component
public class HandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected  Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        //拿到当前匹配用户的clientId，优先使用前端传入的 clientId 保持 WS 与 HTTP 的同一用户ID
        String query = request.getURI().getQuery();

        //取出clientId
        if(query != null){
            for(String part : query.split("&")){
                String[] kv = part.split("=" ,2);
                if(kv.length == 2 && kv[0].equals("clientId") && !kv[1].isEmpty()){
                    final String name = kv[1];
                    return new Principal() { @Override public String getName() { return name; } };
                }
            }

        }
        //如果clientId为空就从cookie中取出JESSIONID作为name
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
        //如果jsid = null就随机分配一个
        final String name = jsid != null ? ("http-" + jsid) : ("u-" + UUID.randomUUID());
        return new Principal() { @Override public String getName() { return name; } };

    }
}
