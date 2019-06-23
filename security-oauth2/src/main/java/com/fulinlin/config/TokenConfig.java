package com.fulinlin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @program: SpringSecurity
 * @author: fulin
 * @create: 2019-06-06 18:20
 **/
@Configuration
public class TokenConfig {

    @Configuration
    public static class JwtTokenConfig {
        @Bean
        public TokenStore jwtTokenStore() {
            return new JwtTokenStore(jwtAccessTokenConverter());
        }

        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter() {
            //这里设置 jwt的密签
            JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
            jwtAccessTokenConverter.setSigningKey("fulin");
            return jwtAccessTokenConverter;
        }

    }

   /* @Configuration //redis的方式
   public static class RedisTokenConfig {
       @Autowired
       private RedisConnectionFactory redisConnectionFactory;

       public TokenStore redisTokenStore() {
           //
           return new RedisTokenStore(redisConnectionFactory);
       }
   }*/
}


