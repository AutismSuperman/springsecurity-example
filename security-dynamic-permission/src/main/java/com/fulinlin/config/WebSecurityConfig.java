package com.fulinlin.config;

import com.fulinlin.authentication.AccessDeniedAuthenticationHandler;
import com.fulinlin.authentication.FailureAuthenticationHandler;
import com.fulinlin.authentication.SuccessAuthenticationHandler;
import com.fulinlin.custom.DynamicFilterInvocationSecurityMetadataSource;
import com.fulinlin.custom.MyDynamicVoter;
import com.fulinlin.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.annotation.Jsr250Voter;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final FailureAuthenticationHandler failureAuthenticationHandler;
    private final SuccessAuthenticationHandler successAuthenticationHandler;
    private final AccessDeniedAuthenticationHandler accessDeniedAuthenticationHandler;

    private final UserService userService;

    public WebSecurityConfig(UserService userService, FailureAuthenticationHandler failureAuthenticationHandler, SuccessAuthenticationHandler successAuthenticationHandler, AccessDeniedAuthenticationHandler accessDeniedAuthenticationHandler) {
        this.userService = userService;
        this.failureAuthenticationHandler = failureAuthenticationHandler;
        this.successAuthenticationHandler = successAuthenticationHandler;
        this.accessDeniedAuthenticationHandler = accessDeniedAuthenticationHandler;
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


    /**
     * 加密解密 这里是自定义MD5
     * security内置了多种加密
     * 可以直接new 接口出来
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(
                new PasswordEncoder() {
                    @Override
                    public String encode(CharSequence charSequence) {
                        return charSequence.toString();
                    }

                    @Override
                    public boolean matches(CharSequence charSequence, String s) {
                        return s.equals(charSequence.toString());
                    }
                });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.httpBasic()  //httpBasic 登录
        http.formLogin()
                .failureHandler(failureAuthenticationHandler) // 自定义登录失败处理
                .successHandler(successAuthenticationHandler) // 自定义登录成功处理
                .and()
                .logout()
                .logoutUrl("/logout")
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/authentication/form") // 自定义登录路径
                .and()
                .authorizeRequests()// 对请求授权
                // 自定义FilterInvocationSecurityMetadataSource
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                        fsi.setSecurityMetadataSource(mySecurityMetadataSource(fsi.getSecurityMetadataSource()));
                        fsi.setAccessDecisionManager(accessDecisionManager());
                        return fsi;
                    }
                })
                .antMatchers("/login", "/authentication/require",
                        "/authentication/form").permitAll()// 这些页面不需要身份认证,其他请求需要认证
                .anyRequest()
                .authenticated().and().exceptionHandling().accessDeniedHandler(accessDeniedAuthenticationHandler)
                .and()
                .csrf().disable();// 禁用跨站攻击
    }


    @Bean
    public DynamicFilterInvocationSecurityMetadataSource mySecurityMetadataSource(FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource) {
        return new DynamicFilterInvocationSecurityMetadataSource(filterInvocationSecurityMetadataSource);
    }


    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
        decisionVoters.add(new AuthenticatedVoter());
        decisionVoters.add(new WebExpressionVoter());
        decisionVoters.add(new MyDynamicVoter());
        return new AffirmativeBased(decisionVoters);
    }
}