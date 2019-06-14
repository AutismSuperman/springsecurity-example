package com.fulinlin.custom;

import org.springframework.stereotype.Service;

/**
 * @program: SpringSecurity
 * @author: fulin
 * @create: 2019-05-29 23:55
 **/
@Service("testService")
public class TestServiceImpl implements TestService {

    public boolean check( String a) {
        System.out.println("进入了自定义的匹配器" + a);
        return true;
    }
}
