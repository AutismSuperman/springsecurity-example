package com.fulinlin.config;


import com.fulinlin.authorize.manager.AuthorizeConfigManager;
import com.fulinlin.service.UserService;
import com.fulinlin.util.MD5Util;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 验证安全核心配置
 */
@Configuration
public class ValidateSecurityCoreConfig extends WebSecurityConfigurerAdapter {
    /**
     * 失败处理器
     */
    private final AuthenticationFailureHandler authenticationFailureHandler;
    /**
     * 成功处理器
     */
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    /**
     * 封装管理器
     */
    private final AuthorizeConfigManager authorizeConfigManager;

    private final UserService userService;

    public ValidateSecurityCoreConfig(AuthenticationFailureHandler authenticationFailureHandler, AuthenticationSuccessHandler authenticationSuccessHandler, AuthorizeConfigManager authorizeConfigManager, UserService userService) {
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authorizeConfigManager = authorizeConfigManager;
        this.userService = userService;
    }


    /**
     * 注入身份管理器bean
     *
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(
                new PasswordEncoder() {

                    @Override
                    public String encode(CharSequence rawPassword) {
                        return MD5Util.encode((String) rawPassword);
                    }

                    @Override
                    public boolean matches(CharSequence rawPassword, String encodedPassword) {
                        return encodedPassword.equals(MD5Util.encode((String) rawPassword));
                    }
                });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginProcessingUrl("/authentication")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()
                // 先加上这句话，否则登录的时候会出现403错误码，Could not verify the provided CSRF token because your session was not found.
                .csrf().disable();
        // 一定要放到最后，是因为config方法里最后做了其他任何方法都需要身份认证才能访问。
        // 放到前面的话，后面在加载.antMatchers(getPermitUrls()).permitAll()的时候也会被认为无权限，
        // 因为前面已经做了其他任何方法都需要身份认证才能访问，SpringSecurity是有先后顺序的。
        authorizeConfigManager.config(http);
    }
}
