package com.fulinlin.sso;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.concurrent.TimeUnit;

/**
 * @program: SpringSecurity
 * @author: fulin
 * @create: 2019-06-04 14:08
 **/
@Configuration
@EnableAuthorizationServer
public class SsoAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 配置认证服务器需要给哪些应用发令牌，我们的例子中是fulin1和fulin2
        clients.inMemory()
                .withClient("fulin1") // clientId
                .secret(new BCryptPasswordEncoder().encode("fulinlin")) // clientSecret
                .authorizedGrantTypes("authorization_code", "refresh_token") // 支持的授权模式
                .scopes("all")
                .autoApprove(false)
                .and()
                .withClient("fulin2")
                .secret(new BCryptPasswordEncoder().encode("fulinlin"))
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("all")
                .autoApprove(false)
        ;
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(jwtTokenStore()).accessTokenConverter(jwtAccessTokenConverter());
        DefaultTokenServices tokenServices = (DefaultTokenServices) endpoints.getDefaultAuthorizationServerTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(1)); // 一天有效期
        endpoints.tokenServices(tokenServices);
    }


    // 认证服务器的安全配置，配置的
    // isAuthenticated() 是SpringSecurity的授权表达式，
    // 默认是 denyAll()，拒绝所有访问
    // 它的意思是访问访问认证服务器的tokenKey的时候，需要进行认证
    // tokenKey 就是对 JWT进行签名的key，即imooc
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("isAuthenticated()");
    }


    /**
     * 下面两个Bean是配置JWT令牌的
     *
     * @return
     */
    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("fulin");
        return converter;
    }


}

