package com.fulinlin.custom;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * @program: SpringSecurity
 * @author: fulin
 * @create: 2019-05-29 23:55
 **/
interface TestPermissionEvaluator {
    boolean check(Authentication authentication);
}

@Service("testPermissionEvaluator")
public class TestPermissionEvaluatorImpl implements TestPermissionEvaluator {

    public boolean check(Authentication authentication) {
        System.out.println("进入了自定义的匹配器" + authentication);
        return false;
    }
}
