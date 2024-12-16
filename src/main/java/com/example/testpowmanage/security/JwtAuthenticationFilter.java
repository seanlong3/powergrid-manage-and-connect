package com.example.testpowmanage.security;

import com.example.testpowmanage.utli.JwtTokenProvider;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;    //自定义的类，负责 JWT 的生成、解析和验证
    private final UserDetailsService userDetailsService;//Spring 提供的服务，用于加载用户的详细信息（如用户名、密码、权限等）。

    @Value("${jwt.authorization-header}")
    public String authorizationHeader;  //从配置文件中获取 JWT 令牌所在的 HTTP 请求头字段名称，通常是 Authorization。

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain securityFilterChain) throws ServletException, IOException {
//        System.out.println(securityFilterChain);
        // 获取请求头中的 JWT 令牌
//        String token =null;
//        String header = request.getHeader(authorizationHeader);
//        if (StringUtils.hasLength(header)){
//            token = header.split(" ")[1];
            //如果想使用postman进行接口测试，只能通过这种方法先把头砍掉，再把返回信息中的token给postman的authorization
//            从请求的 HTTP 头部（通常为 Authorization）中获取 JWT 令牌，头部的格式一般为 Bearer <JWT>，所以通过 split(" ")[1] 提取实际的 JWT 令牌。
//        }
        String token = request.getHeader(authorizationHeader);
        // 验证 JWT 令牌
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)){
            String username = jwtTokenProvider.getUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);//加载用户详细信息，通过从数据库或其他存储系统中加载该用户的详细信息，包括权限信息（如角色）。
           //创建认证对象：UsernamePasswordAuthenticationToken 是 Spring Security 的一个认证凭证对象，表示用户已经通过认证，并将用户的详细信息（userDetails）以及权限存入其中。
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);  //将认证对象存入当前的安全上下文，意味着用户已经成功通过认证
        }
        securityFilterChain.doFilter(request, response);
    }
}
