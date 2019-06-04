package com.fulinlin.authentication;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
@Slf4j
public class SuccessAuthenticationHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    /*
     * 1.从request的请求头中拿到Authorization信息
     * 2.根据clientId获取到secret和请求头中的secret信息做对比
     * 3.如果正确，组建一个新的TokenRequest类
     * 4.根据前者和clientDetails创建OAuth2Request对象
     * 5.根据前者和authentication创建OAuth2Authentication对象
     * 6.通过AuthorizationServerTokenServices和前者前者创建OAuth2AccessToken对象
     * 7.将token返回
     * */

    @Autowired
    private AuthorizationServerTokenServices jwkTokenServices;

    @Autowired
    private ClientDetailsService clientDetailsService;

    public SuccessAuthenticationHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("登录成功");
        //从header看看有没有Authorization
        String header = request.getHeader("Authorization");
        if (StringUtils.isBlank(header) && !header.startsWith("Basic")) {
            throw new UnapprovedClientAuthenticationException("请求投中无client信息");
        }
        String[] tokens = this.extractAndDecodeHeader(header, request);
        assert tokens.length == 2;
        //获取clientId 和 clientSecret
        String clientId = tokens[0];
        String clientSecret = tokens[1];
        //获取 ClientDetails
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        if (clientDetails == null) {
            throw new UnapprovedClientAuthenticationException("clientId 不存在" + clientId);
            //判断  自己的clientid和 clientsecret  是否一致
        } else if (!StringUtils.equals(clientDetails.getClientSecret(), clientSecret)) {
            throw new UnapprovedClientAuthenticationException("clientSecret 不匹配" + clientId);
        }
        //密码授权 模式, 组建 authentication
        TokenRequest tokenRequest = new TokenRequest(Collections.emptyMap(), clientId, clientDetails.getScope(), "password");
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        OAuth2AccessToken token = jwkTokenServices.createAccessToken(oAuth2Authentication);
        //将 authention 信息打包成json格式返回
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(token));
    }


    /**
     * 解码请求头
     */
    private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException {
        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);

        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException var7) {
            throw new BadCredentialsException("你传的令牌有点另类啊");
        }

        String token = new String(decoded, StandardCharsets.UTF_8);
        int delim = token.indexOf(":");
        if (delim == -1) {
            throw new BadCredentialsException("你传的令牌有点另类啊");
        } else {
            return new String[]{token.substring(0, delim), token.substring(delim + 1)};
        }
    }
}

