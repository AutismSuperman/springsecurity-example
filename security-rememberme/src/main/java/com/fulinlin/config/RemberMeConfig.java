package com.fulinlin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @program: spring-seruirty
 * @author: Fulin
 * @create: 2019-05-29 15:58
 **/
@Configuration
public class RemberMeConfig {

    private final DataSource dataSource;

    public RemberMeConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // 如果token表不存在，使用下面语句可以初始化该表；若存在，请注释掉这条语句，否则会报错。
        //tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

}
