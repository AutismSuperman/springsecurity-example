package com.fulinlin.exception.oauth2;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

@JsonSerialize(using = MyOAuthExceptionJacksonSerializer.class)
public class MyOAuth2Exception extends OAuth2Exception {
    public MyOAuth2Exception(String msg, Throwable t) {
        super(msg, t);
    }

    public MyOAuth2Exception(String msg) {
        super(msg);
    }
}	
