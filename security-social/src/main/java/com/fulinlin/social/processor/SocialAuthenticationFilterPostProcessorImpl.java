package com.fulinlin.social.processor;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @program: spring-seruirty
 * @author: Fulin
 * @create: 2019-06-19 16:03
 * 在oauth2 密码模式的时候成功处理器可以注入进来
 **/
@Component
public class SocialAuthenticationFilterPostProcessorImpl implements SocialAuthenticationFilterPostProcessor {
    @Override
    public void process(SocialAuthenticationFilter socialAuthenticationFilter) {
        //重置社交登陆的成功处理器
        //socialAuthenticationFilter.setAuthenticationSuccessHandler();
    }
}
