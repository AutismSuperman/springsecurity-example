package com.fulinlin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: spring-seruirty
 * @author: Fulin
 * @create: 2019-05-28 16:03
 **/
@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login() {
        return "login";
    }
    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }
}
