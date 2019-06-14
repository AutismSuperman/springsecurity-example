package com.fulinlin.config;

import com.fulinlin.properties.QQProperties;
import com.fulinlin.qq.factory.QQConnectionFactory;
import com.fulinlin.qq.support.SocialAuthenticationFilterPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
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
    private QQProperties qqProperties;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;

    /**
     * 关联用户登陆的持久化
     *
     * @param connectionFactoryLocator
     * @return
     */
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
    }

    /**
     * 根据不同的ProviderId创建不通的连接工厂
     *
     * @param connectionFactoryConfigurer
     * @param environment
     */
    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
        connectionFactoryConfigurer.addConnectionFactory(createConnectionFactory());
    }

    private ConnectionFactory<?> createConnectionFactory() {
        return new QQConnectionFactory(qqProperties.getProviderId(), qqProperties.getAppid(), qqProperties.getAppSecret());
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
        //登陆的地址
        configurer.signupUrl("/qqLogin");
        configurer.setSocialAuthenticationFilterPostProcessor(socialAuthenticationFilterPostProcessor);
        return configurer;
    }

}
