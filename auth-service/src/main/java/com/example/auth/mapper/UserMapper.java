package com.example.auth.mapper;

import com.example.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);
    
    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email);
    
    @Select("SELECT * FROM users WHERE reset_token = #{token}")
    User findByResetToken(String token);
    
    @Update("UPDATE users SET reset_token = #{resetToken}, reset_token_expires_at = #{resetTokenExpiresAt} WHERE id = #{id}")
    void updateResetToken(User user);
    
    @Update("UPDATE users SET password = #{password}, reset_token = NULL, reset_token_expires_at = NULL WHERE id = #{id}")
    void updatePassword(User user);
    
    @Select("INSERT INTO users (username, password, email, enabled, roles) VALUES (#{username}, #{password}, #{email}, #{enabled}, #{roles})")
    void save(User user);
}
