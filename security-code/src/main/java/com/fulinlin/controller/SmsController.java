package com.fulinlin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: spring-seruirty
 * @author: Fulin
 * @create: 2019-05-28 15:30
 **/
@RestController
@Slf4j
public class SmsController {

    @RequestMapping("/sms/code")
    public String sms(String mobile, HttpSession session) {
        int code = (int) Math.ceil(Math.random() * 9000 + 1000);
        Map<String, Object> map = new HashMap<>(16);
        map.put("mobile", mobile);
        map.put("code", code);
        session.setAttribute("smsCode", map);
        log.info("{}：为 {} 设置短信验证码：{}", session.getId(), mobile, code);
        return "你的手机号"+mobile+"验证码是"+code;
    }


}
