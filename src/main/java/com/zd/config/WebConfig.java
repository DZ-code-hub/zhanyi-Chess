package com.zd.config;

import com.zd.Interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加jwt拦截器
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**") // 只拦截/api路径
                .excludePathPatterns("/api/login", "/api/register"); // 排除登录注册
    }
}
