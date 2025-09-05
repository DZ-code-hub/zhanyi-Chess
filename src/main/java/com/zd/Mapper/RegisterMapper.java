package com.zd.Mapper;

import com.zd.Entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
//注册功能mapper映射
@Mapper
public interface RegisterMapper {
    //通过用户名判断是否存在，存在返回user对象，不存在返回null
    @Select("select * from user where username = #{username}")
     User existsByUsername(String username) ;

    //通过邮箱判断是否存在，存在返回user对象，不存在返回null
    @Select("select * from user where email = #{email}")
     User existsByEmail(String email) ;

    //注册成功，向数据库中存储账号
    @Insert("insert into user(username,password,email)" + "values" +"(#{username},#{password},#{email})")
     void saveUser(User user) ;

}
