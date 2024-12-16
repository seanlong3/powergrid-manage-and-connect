package com.example.testpowmanage.utli;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Log4j2
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;//jwt密钥，用于生成和验证jwt的签名
    @Value("${jwt.expiration-milliseconds}")
    private long jwtExpirationTime;//表示token的有效期

    //生成一个用于签名的 SecretKey 对象。通过 Base64 解码从配置中加载的 jwtSecret，并使用 hmacShaKeyFor 生成 HMAC-SHA 算法的签名密钥
    private SecretKey signKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    //辅助用法，从jwt token中获取用户名
    public String getUsername(String token) {
        // 解析 JWT token 并获取 Claims 对象,即：jwt的负载部分，并使用密钥 signKey() 来验证签名的有效性
        Claims claims = Jwts.parser()
                .verifyWith(signKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();  //返回 JWT 的有效负载（Claims）
        // 从 Claims 中获取 subject (即用户名)
        return claims.getSubject();
    }

    //主用法，生成jwt令牌
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + jwtExpirationTime);
        return Jwts.builder()
                .subject(username)//token的主题，用户名
                .issuedAt(currentDate)//签发时间
                .expiration(expirationDate)//过期时间
                .signWith(signKey())//使用签名密钥进行签名
                .compact();//生成并返回token
    }

    //主用法，验证token有效性
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(signKey())
                    .build()
                    .parse(token);//解析token
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());//jwt格式不正确
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());//jwt过期
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());//jwt使用了不支持的方式生成
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());//传递给解释器的token是null或空字符串
        }
        return false;
    }


}
