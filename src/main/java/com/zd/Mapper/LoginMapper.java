package com.zd.Mapper;

import com.zd.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper {

    @Select("select * from user where username = #{username} and password = #{password}")
    User selectByUsernameAndPassword(String username, String  password);
}

