package com.fulinlin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: spring-seruirty
 * @author: Fulin
 * @create: 2019-05-13 16:41
 **/
@Controller
public class TestController {


    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }
}
