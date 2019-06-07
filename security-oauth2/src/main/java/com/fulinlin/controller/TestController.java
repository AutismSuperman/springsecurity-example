package com.fulinlin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: SpringSecurity
 * @author: fulin
 * @create: 2019-06-06 18:19
 **/
@RestController
public class TestController {

    @RequestMapping("/hello")
    public String hello() {
        return "this hello";
    }
}
