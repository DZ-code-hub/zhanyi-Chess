package com.zd.Service;

import com.zd.Entity.User;
import com.zd.Mapper.RegisterMapper;
import com.zd.dto.RegisterRequest;
import com.zd.dto.RegisterResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegisterServiceImpl implements RegisterService{
    @Autowired
    RegisterMapper registerMapper;
    public RegisterResponse Register(RegisterRequest request) {


        //创建一个user对象
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        log.info("注册成功：{}",user);
        //将新添加的用户存入数据库中
        registerMapper.saveUser(user);
        //返回注册成功的响应
        return new RegisterResponse("注册成功",true);

    }
}
