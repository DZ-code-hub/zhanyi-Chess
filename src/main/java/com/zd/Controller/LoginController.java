package com.zd.Controller;

import com.zd.Service.LoginService;
import com.zd.dto.LoginRequest;
import com.zd.dto.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class LoginController {

    @Autowired
    LoginService loginService;
    //用户登录接口
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        log.info("登录接口调用，登录请求为：{}",loginRequest);
        LoginResponse reponse = loginService.login(loginRequest);
        if(reponse.getSuccess()){
            return ResponseEntity.ok(reponse);
        }
        return ResponseEntity.badRequest().body(reponse);

    }

}
