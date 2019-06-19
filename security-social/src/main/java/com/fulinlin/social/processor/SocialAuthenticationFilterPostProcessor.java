package com.fulinlin.social.processor;

import org.springframework.social.security.SocialAuthenticationFilter;

/**
 * @program: spring-seruirty
 * @author: Fulin
 * @create: 2019-06-19 16:01
 **/
public interface SocialAuthenticationFilterPostProcessor {

    void process(SocialAuthenticationFilter socialAuthenticationFilter);

}
