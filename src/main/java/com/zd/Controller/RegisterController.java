package com.zd.Controller;

import com.zd.Mapper.RegisterMapper;
import com.zd.Service.RegisterService;
import com.zd.dto.RegisterRequest;
import com.zd.dto.RegisterResponse;
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
public class RegisterController {

    @Autowired
    RegisterService registerService;
    @Autowired
    RegisterMapper registerMapper;
    //用户注册接口
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> userRegister(@RequestBody RegisterRequest request){
        //打印请求信息
        log.info("用户注册请求接口调用：{}",request);

        //判断用户名是否存在
        if(registerMapper.existsByUsername(request.getUsername()) != null){
            log.info("用户名已存在:{}",request.getUsername());
            return ResponseEntity.badRequest()
                            .body(new RegisterResponse("用户名已存在",false)) ;
        }
        //判断邮箱是否存在
        if(registerMapper.existsByEmail(request.getEmail()) != null){
            log.info("邮箱已被注册:{}",request.getEmail());
            return ResponseEntity.badRequest()
                    .body(new RegisterResponse("邮箱已被注册",false));
        }

        //调用service层方法
        RegisterResponse response = registerService.Register(request);
        return ResponseEntity.ok(response);
    }
}
