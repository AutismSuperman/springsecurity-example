package com.fulinlin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @program: SpringSecurity
 * @author: fulin
 * @create: 2019-06-04 14:05
 **/
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SsoServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SsoServerApplication.class, args);
    }
}
