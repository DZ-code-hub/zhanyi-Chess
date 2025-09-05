package com.zd.Mapper;

import com.zd.Entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RegisterMapper {

    @Select("select * from user where username = #{username}")
     User existsByUsername(String username) ;

    @Select("select * from user where email = #{email}")
     User existsByEmail(String email) ;

    @Insert("insert into user(username,password,email)" + "values" +"(#{username},#{password},#{email})")
     void saveUser(User user) ;

}
