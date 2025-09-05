package com.zd.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//jwt工具类
@Component
public class JWTUtil {
    //设置jwt密钥
    private static final String SECRET_KEY = "zhanyi-chess-jwt-secret-key-" +
                                     "must-be-at-least-256-bits-long-for-security";
    //设置过期时间
    private static final Long EXPIRATION_TIME = 24 * 60 * 60 * 1000L;
    //设置加密算法
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    //生成jwt TOKEN
    public String generateToken(String username,Long userId){
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",userId);
        claims.put("username",username);
        claims.put("timestmp",System.currentTimeMillis());

         /*
        * setClaims(claims)
        设置 JWT 的声明部分，包含之前添加的 userId、username 和 timestamp。
        .setSubject(username)
        设置 JWT 的主题（subject），这里设置为用户名。
        .setIssuedAt(new Date())
        设置 JWT 的签发时间（issued at）为当前时间。
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        设置 JWT 的过期时间，为当前时间加上预设的过期时间（24小时）。
        .signWith(key, SignatureAlgorithm.HS256)
        使用之前生成的密钥和 HS256 算法对 JWT 进行签名。
        .compact();
        将构建好的 JWT 转换为紧凑的、URL 安全的字符串格式并返回。*/
        return  Jwts.builder().setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    /*
     * 解析Token获取claims
     * */
    public Claims extractClaims(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /*从token中提取用户名*/
    public String extractUsername(String token){
        Claims claims = extractClaims(token);
        return claims == null ? null : claims.getSubject();
    }

    /*从token中提取用户ID*/
    public Long extractUserId(String token){
        Claims claims = extractClaims(token);
        return claims != null ? claims.get("userId", Long.class) : null;
    }

    /*验证token是否有效*/
    public boolean validateToken(String token){
        try{
            Claims claims = extractClaims(token );
            if(claims == null)
                return false;
            return !claims.getExpiration().before(new Date());
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /*检查token是否即将过期（用于自动刷新）*/
    public boolean isTokenNearExpiration(String token){
        Claims claims = extractClaims(token);
        if(claims == null) return false;

        Long oneHour = 60 * 60 * 1000L;
        return claims.getExpiration().getTime() - System.currentTimeMillis() < oneHour;
    }




}
