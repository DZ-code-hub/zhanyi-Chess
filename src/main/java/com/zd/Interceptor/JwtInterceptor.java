package com.zd.Interceptor;

import com.zd.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
//jwt握手拦截器
@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    JWTUtil jwtUtil;

    //握手前进行拦截，将前端静态资源排除在外
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if(uri.contains("/api/login") ||
           uri.contains("/api/register") ||
           uri.contains(".html") ||
           uri.contains(".css") ||
           uri.contains(".js") ||
           uri.contains("/ws")) {
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
            return true;
        }
        // Token无效，返回401错误
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write("{\"success\":false,\"message\":\"未授权访问，请先登录\"}");
        writer.flush();
        return false;

    }


}
