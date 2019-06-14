package com.fulinlin.qq.template;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * @program: spring-seruirty
 * @author: Fulin
 * @create: 2019-06-14 15:56
 **/
public class QQOAuth2Template extends OAuth2Template {
    public QQOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        /**
         * 发请求的时候携带参数
         */
        setUseParametersForClientAuthentication(true);
    }


    /**
     * 处理QQ特殊的返回成功信息,不是json的
     *
     * @param accessTokenUrl
     * @param parameters
     * @return
     */
    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        //获取accessToke
        String responseStr = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class);
        /*
         * 截取认证成功后的信息
         */
        String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(responseStr, "&");
        /*
         *
         * accessToken
         */
        String accessToken = StringUtils.substringAfterLast(items[0], "=");
        /*
         * expiresIn
         */
        Long expiresIn = new Long(StringUtils.substringAfterLast(items[1], "="));
        /*
         * 刷新令牌
         */
        String refreshToken = StringUtils.substringAfterLast(items[2], "=");
        return super.postForAccessGrant(accessTokenUrl, parameters);
    }

    /**
     * 微信返回的contentType是html/text，添加相应的HttpMessageConverter来处理。
     */
    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }
}
