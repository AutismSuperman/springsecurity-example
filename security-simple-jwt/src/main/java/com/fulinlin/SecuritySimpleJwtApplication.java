package com.fulinlin;

import com.fulinlin.properties.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @program: security-test
 * @author: fulin
 * @create: 2019-11-17 14:32
 **/
@SpringBootApplication
@EnableConfigurationProperties(SecurityProperties.class)
public class SecuritySimpleJwtApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecuritySimpleJwtApplication.class, args);
    }
}
