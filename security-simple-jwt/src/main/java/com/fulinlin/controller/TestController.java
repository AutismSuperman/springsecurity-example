package com.fulinlin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: security-test
 * @author: fulin
 * @create: 2019-11-17 18:15
 **/
@RestController
public class TestController {
    @RequestMapping("/test")
    public String test() {
        return "111";
    }
}
