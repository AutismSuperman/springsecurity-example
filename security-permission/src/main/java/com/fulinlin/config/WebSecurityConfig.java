package com.fulinlin.config;

import com.fulinlin.authentication.AccessDeniedAuthenticationHandler;
import com.fulinlin.authentication.CustomPermissionEvaluator;
import com.fulinlin.authentication.FailureAuthenticationHandler;
import com.fulinlin.authentication.SuccessAuthenticationHandler;
import com.fulinlin.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled=true,jsr250Enabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /*
     * @EnableGlobalMethodSecurity 这注解的意思是是否开启注解spring security 的注解权限
     *
     * prePostEnabled=true :启用 Spring Security
     * 主要注解
     * @PreAuthorize    --适合进入方法之前验证授权
     * @PostAuthorize   --检查授权方法之后才被执行
     * @PostFilter --在方法执行之后执行，而且这里可以调用方法的返回值，然后对返回值进行过滤或处理或修改并返回
     * @PreFilter --在方法执行之前执行，而且这里可以调用方法的参数，然后对参数值进行过滤或处理或修改
     *
     * 利用JAVA8的参数名反射特性可以直接子注解中调用方法，如果没用JAVA8
     * Spring EL提供可在表达式语言来访问并从方法返回 returnObject 对象来反映实际的对象
     * 这俩注解可以兼顾，角色/登录用户权限，参数传递给方法等等。
     *
     * 例子
     *  	@PostAuthorize ("returnObject.type == authentication.name")
     *      @PreAuthorize("hasRole('ADMIN')")
     *      @PreAuthorize("hasRole('ADMIN') AND hasRole('DBA')")
     *
     * ================================================
     * securedEnabled=true : 启用 Spring Security 安全注释
     * 主要注解
     * @Secured
     *
     * @Secured注释是用来定义业务方法的安全性配置属性列表
     * 可以使用@Secured在方法上指定安全性要求 角色/权限等
     * 只有对应 角色/权限 的用户才可以调用这些方法。
     * 如果有人试图调用一个方法，但是不拥有所需的 角色/权限，那会将会拒绝访问将引发异常。
     * @Secured ,不支持Spring EL表达式
     * 例子
     *      @Secured("ROLE_ADMIN")
     *      @Secured({ "ROLE_DBA", "ROLE_ADMIN" })
     *
     * ================================================
     * jsr250Enabled=true : 启用 JSR-250注释
     * 主要注解
     * @DenyAll 拒绝所有访问
     * @RolesAllowed
     * @PermitAll
     *
     *  @RolesAllowed({"USER", "ADMIN"})
     *  该方法只要具有"USER", "ADMIN"任意一种权限就可以访问。这里可以省略前缀ROLE_，实际的权限可能是ROLE_ADMIN
     *
     *
     * */

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
     * 注入自定义权限管理
     *
     * @return
     * @throws Exception
     */
    @Bean
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(new CustomPermissionEvaluator());
        return handler;
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
                .antMatchers("/login", "/authentication/require",
                        "/authentication/form").permitAll()// 这些页面不需要身份认证,其他请求需要认证
                .anyRequest()
                .authenticated().and().exceptionHandling().accessDeniedHandler(accessDeniedAuthenticationHandler)
                .and()
                .csrf().disable();// 禁用跨站攻击
    }
}