package com.zd.Interceptor;

import com.zd.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

//jwt握手拦截器
@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    JWTUtil jwtUtil;
    @Autowired
    StringRedisTemplate redisTemplate;

    //握手前进行拦截，将前端静态资源排除在外
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if(uri.contains("/api/login") ||
           uri.contains("/api/register") ||
           uri.contains(".html") ||
           uri.contains(".css") ||
           uri.contains(".js") ||
           uri.contains("/ws") ||
           uri.contains("/api/match")) {
            return true;
        }

        //从请求头获取token
        String token = request.getHeader("Authorization");
        //token存在且有效
        if(token != null && token.startsWith("Bearer")){
            token = token.substring(7);
        }
        if(token != null && jwtUtil.validateToken(token)){
            request.setAttribute("userId",jwtUtil.extractUserId(token));
            request.setAttribute("username",jwtUtil.extractUsername(token));
            String username = jwtUtil.extractUsername(token);
            String redisKey = "zd:username:" + username;
            //从redis中取出token与前端传来的匹配，相等则校验成功
            String s = redisTemplate.opsForValue().get(redisKey);
            log.info("客户端传来的token：{}",token);
            log.info("redis中的token：{}",s);
            if(token.equals(s)){
                redisTemplate.opsForValue().set(redisKey,token,30, TimeUnit.MINUTES);
                return true;
            }

        }
        log.info("拦截器生效，拦截路径为：{}",uri);
        // Token无效，返回401错误
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write("{\"success\":false,\"message\":\"未授权访问，请先登录\"}");
        writer.flush();
        return false;

    }


}