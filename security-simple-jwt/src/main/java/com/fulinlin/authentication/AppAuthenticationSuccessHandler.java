package com.fulinlin.authentication;

import com.alibaba.fastjson.JSON;
import com.fulinlin.filter.JwtAuthenticationTokenFilter;
import com.fulinlin.jwt.JwtTokenUtil;
import com.fulinlin.properties.SecurityProperties;
import com.fulinlin.util.GenerateModelMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证成功后处理器
 * 不管是什么认证 表单或者短信 都会到这里
 */
@Slf4j
@Component
public class AppAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        final String randomKey = jwtTokenUtil.getRandomKey();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        log.info("username：{}", username);
        //生产JWT 令牌
        final String token = jwtTokenUtil.generateToken(username, randomKey);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(username, randomKey);
        log.info("登录成功！");
        ModelMap modelMap = GenerateModelMap.generateMap(HttpStatus.OK.value(), "登陆成功");
        modelMap.put("token", JwtAuthenticationTokenFilter.TOKEN_PREFIX + token);
        modelMap.put("refreshToken", JwtAuthenticationTokenFilter.TOKEN_PREFIX + refreshToken);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(modelMap));
    }
}
