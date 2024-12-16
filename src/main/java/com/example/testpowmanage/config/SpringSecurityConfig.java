package com.example.testpowmanage.config;

import com.example.testpowmanage.security.JwtAuthenticationFilter;
import com.example.testpowmanage.security.PermissionAuthenticationManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Slf4j
public class SpringSecurityConfig {
    //管理所有的认证提供者
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    //BCryptPasswordEncoder 对密码进行加密和验证
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            PermissionAuthenticationManager permissionAuthenticationManager
    ) throws Exception {
        httpSecurity.csrf(httpSecurityCsrfConfigurer -> {
                    try {
                        httpSecurityCsrfConfigurer.disable()    //禁用了 CSRF 保护机制。通常在使用 JWT 时，CSRF 是不必要的，因为 JWT 自身具备保护机制。
                                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                                    authorizationManagerRequestMatcherRegistry.requestMatchers("/auth/**").permitAll();     //定义了 /auth/** 路径下的请求不需要认证，允许所有请求通过。这通常用于登录或注册等公开 API。，其实这里也可以同时把注册register接口
                                    authorizationManagerRequestMatcherRegistry.anyRequest().access(permissionAuthenticationManager);    //对其他所有请求使用 PermissionAuthenticationManager 进行自定义的权限控制。
                                });
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                //将自定义的 JwtAuthenticationFilter 添加到过滤器链中，并且它位于 UsernamePasswordAuthenticationFilter之前
        DefaultSecurityFilterChain build = httpSecurity.build();
        System.out.println(build);
        return build;
    }


}
