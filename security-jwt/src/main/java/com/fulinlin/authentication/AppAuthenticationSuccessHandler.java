package com.fulinlin.authentication;

import com.alibaba.fastjson.JSON;
import com.fulinlin.enums.JwtRedisEnum;
import com.fulinlin.jwt.JwtTokenUtil;
import com.fulinlin.properties.SecurityProperties;
import com.fulinlin.util.GenerateModelMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 认证成功后处理器
 * 不管是什么认证 表单或者短信 都会到这里
 */
@Slf4j
public class AppAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        final String randomKey = jwtTokenUtil.getRandomKey();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        log.info("username：{}", username);
        final String token = jwtTokenUtil.generateToken(username, randomKey);
        log.info("登录成功！");
        // 判断是否开启允许多人同账号同时在线，若不允许的话则先删除之前的
        if (securityProperties.getJwt().isPreventsLogin()) {
            // T掉同账号已登录的用户token信息
            batchDel(JwtRedisEnum.getTokenKey(username, "*"));
            // 删除同账号已登录的用户认证信息
            batchDel(JwtRedisEnum.getAuthenticationKey(username, "*"));
        }
        // 存到redis
        redisTemplate.opsForValue().set(JwtRedisEnum.getTokenKey(username, randomKey),
                token,
                securityProperties.getJwt().getExpiration(),
                TimeUnit.SECONDS);
        //将认证信息存到redis，方便后期业务用，也方便 JwtAuthenticationTokenFilter用
        redisTemplate.opsForValue().set(JwtRedisEnum.getAuthenticationKey(username, randomKey),
                JSON.toJSONString(authentication),
                securityProperties.getJwt().getExpiration(),
                TimeUnit.SECONDS
        );
        ModelMap modelMap = GenerateModelMap.generateMap(HttpStatus.OK.value(), "登陆成功");
        modelMap.put("currentUser", authentication);
        response.setHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(modelMap));

    }

    /**
     * 批量删除redis的key
     *
     * @param key：redis-key
     */
    private void batchDel(String key) {
        Set<String> set = redisTemplate.keys(key);
        for (String keyStr : Objects.requireNonNull(set)) {
            log.info("keyStr{}", keyStr);
            redisTemplate.delete(keyStr);
        }
    }
}
