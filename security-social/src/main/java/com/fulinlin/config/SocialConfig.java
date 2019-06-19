package com.fulinlin.config;

import com.fulinlin.social.processor.SocialAuthenticationFilterPostProcessor;
import com.fulinlin.social.support.MySpringSocialConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * @program: spring-seruirty
 * @author: Fulin
 * @create: 2019-06-14 16:41
 **/
@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;


    @Autowired(required = false)
    private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;

    /**
     * 关联用户登陆的持久化
     *
     * @param connectionFactoryLocator
     * @return
     */
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        //TextEncryptor 这个参数是向数据库插入的时候做加密 这里不做加密
        JdbcUsersConnectionRepository jdbcUsersConnectionRepository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        /* 增加前缀假设需要 */
        //jdbcUsersConnectionRepository.setTablePrefix("fulin_");
        //这里的 connectionSignUp 是做自动登录用了
        //用户只需要实现 connectionSignUp 接口然后里面做自动向数据库
        //插入用户的逻辑并且返回 用户的唯一标识即可
        //这里作为可配置的
        if (connectionSignUp != null) {
            jdbcUsersConnectionRepository.setConnectionSignUp(connectionSignUp);
        }
        return jdbcUsersConnectionRepository;
    }

    /**
     * 社交登录配置类，供浏览器或app模块引入设计登录配置用。
     *
     * @return
     */
    @Bean
    public SpringSocialConfigurer mySpringSocialConfigurer() {
        // SocialAuthenticationFilter过滤器默认拦截的请求是/auth开头，这里获取自己配置的处理路径/login
        //然后你的认证地址就是 /login/qq  这个qq就是你设置的providerId
        MySpringSocialConfigurer configurer = new MySpringSocialConfigurer("/login");
        // 如果实现了connectionSignUp 就不会跳到 注册页面
        // 如果找不到用户 就跳到登陆页面
        configurer.setSocialAuthenticationFilterPostProcessor(socialAuthenticationFilterPostProcessor);
        configurer.signupUrl("/registered");
        return configurer;
    }


    /**
     * 用来处理注册流程的工具类
     * 用于非自动注册
     * 拿到当前社交登陆的用户信息	providerSignInUtils.getConnectionFromSession(rquuest)
     * 然后执行绑定或者注册 就是做数据库增删改查
     * 然后在调用providerSignInUtils.doPostSignUp(rquuest); 执行后续的登陆逻辑
     * 就是SocialUserDetailsService 的 loadUserByUserId 方法
     * 完成整个认证流程
     *
     * @param connectionFactoryLocator
     * @return
     */
    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
        return new ProviderSignInUtils(connectionFactoryLocator,
                getUsersConnectionRepository(connectionFactoryLocator)) {
        };
    }

}
