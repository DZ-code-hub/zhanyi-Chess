package com.zd.Mapper;

import com.zd.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

//登录功能持久层映射文件
@Mapper
public interface LoginMapper {

    //通过用户名和密码查询用户
    @Select("select * from user where username = #{username} and password = #{password}")
    User selectByUsernameAndPassword(String username, String  password);
}

