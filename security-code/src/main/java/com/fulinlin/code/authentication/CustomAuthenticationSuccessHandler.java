package com.fulinlin.code.authentication;

import com.alibaba.fastjson.JSON;
import com.fulinlin.util.GenerateModelMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证成功后处理器
 * 不管是什么认证 表单或者短信 都会到这里
 */
@Slf4j
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("登录成功");
        ModelMap modelMap = GenerateModelMap.generateMap(HttpStatus.OK.value(), "登录成功");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(modelMap));
    }

}
