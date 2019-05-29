package com.fulinlin;

import com.fulinlin.properties.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityJwtApplication.class, args);
    }

}
