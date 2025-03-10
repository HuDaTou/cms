package com.overthinker.cloud.web.config;

import com.overthinker.cloud.web.entity.constants.SecurityConst;
import com.overthinker.cloud.web.filter.JwtAuthorizeFilter;
import com.overthinker.cloud.web.handler.SecurityHandler;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



/**
 * @author overH
 * <p>
 * 创建时间：2023/10/10 16:26
 */
@Configuration
public class SecurityConfiguration {

    @Resource
    private SecurityHandler securityHandler;

    @Resource
    private JwtAuthorizeFilter jwtAuthorizeFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf -> conf
                        // 需要校验的接口
                        .requestMatchers(SecurityConst.AUTH_CHECK_ARRAY).authenticated()
                        // 注册接口
                        // 其他的都不用要校验
                        .anyRequest().permitAll()
                )
                .formLogin(conf -> conf
                        // 登录页面
                        .loginProcessingUrl(SecurityConst.LOGIN_PAGE)
                        // 成功登录处理
                        .successHandler(securityHandler::onAuthenticationSuccess)
                        // 失败登录处理
                        .failureHandler(securityHandler::onAuthenticationFailure)
                )
                .logout(conf -> conf
                        // 登出页面
                        .logoutUrl(SecurityConst.LOGOUT_PAGE)
                        // 退出登录处理
                        .logoutSuccessHandler(securityHandler::onLogoutSuccess)
                )
                .exceptionHandling(conf -> conf
                        // 没有登录处理
                        .authenticationEntryPoint(securityHandler::onUnAuthenticated)
                        // 没有权限处理
                        .accessDeniedHandler(securityHandler::onAccessDeny)
                )
//                TODO 设置跨域
                // 禁用 csrf
                .csrf(AbstractHttpConfigurer::disable)
                // 不处理 session ，使用token
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // token 校验添加过滤器
                .addFilterBefore(jwtAuthorizeFilter, UsernamePasswordAuthenticationFilter.class)
//                .cors(Customizer.withDefaults())
                .build();
    }

}
