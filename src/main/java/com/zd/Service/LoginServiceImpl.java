package com.zd.Service;

import com.zd.Entity.User;
import com.zd.Mapper.LoginMapper;
import com.zd.dto.LoginRequest;
import com.zd.dto.LoginResponse;
import com.zd.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
@Slf4j
public class LoginServiceImpl implements LoginService{
    @Autowired
    LoginMapper loginMapper;
    @Autowired
    JWTUtil jwtUtil;
    //实现登录方法
    public LoginResponse login(LoginRequest loginRequest) {

        User user = loginMapper.selectByUsernameAndPassword(loginRequest.getUsername(),
                                                            loginRequest.getPassword());

        //查询用户名和密码是否匹配
        if(user != null){
//            生成并打印JWT token
            String token = jwtUtil.generateToken(user.getUsername(),user.getId());
            log.info("登陆成功，登录用户id为：{}",user.getId());
            log.info("token为：{}",token);

            return new LoginResponse(
                    token,
                    user.getId(),
                    user.getUsername(),
                    "登录成功",
                    true
            );
        }
        //查询失败，user对象为null
        log.info("登陆失败");
        return new LoginResponse(
                null,
                null,
                null,
                "登陆失败",
                false
        );
    }
}
