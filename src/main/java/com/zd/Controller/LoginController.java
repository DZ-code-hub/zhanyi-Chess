package com.zd.Controller;

import com.zd.Service.LoginService;
import com.zd.dto.LoginRequest;
import com.zd.dto.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
@Slf4j
public class LoginController {

    @Autowired
    LoginService loginService;
    @Autowired
    StringRedisTemplate redisTemplate;
    //用户登录接口
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){

        LoginResponse response = loginService.login(loginRequest);
        if(response.getSuccess()){
            String username = loginRequest.getUsername();
            String token = response.getToken();
            //判断当前登录用户是否已经登录
            String redisKey = "zd:username:" + username;
            if(redisTemplate.opsForValue().get(redisKey) != null){
                //踢掉前面登录的玩家
                redisTemplate.delete(redisKey);
                log.info("该用户已经登陆过，前面的用户该被踢掉");
            }

            //把token和username存到redis，username为主键
            redisTemplate.opsForValue().set(redisKey,token,30, TimeUnit.MINUTES);

            return ResponseEntity.ok(response);
        }


        return ResponseEntity.badRequest().body(response);
    }

}
