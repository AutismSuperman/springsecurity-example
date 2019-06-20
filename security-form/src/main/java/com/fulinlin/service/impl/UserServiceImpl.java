package com.fulinlin.service.impl;

import com.fulinlin.pojo.SysUser;
import com.fulinlin.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: spring-seruirty
 * @author: Fulin
 * @create: 2019-05-13 16:14
 **/
@Service
public class UserServiceImpl implements IUserService {

    private static final Set<SysUser> users = new HashSet<>();


    static {
        users.add(new SysUser(1L, "fulin", "123456", Arrays.asList("ROLE_ADMIN", "ROLE_DOCKER")));
        users.add(new SysUser(2L, "xiaohan", "123456", Arrays.asList("ROLE_ADMIN", "ROLE_DOCKER")));
        users.add(new SysUser(3L, "longlong", "123456", Arrays.asList("ROLE_ADMIN", "ROLE_DOCKER")));
    }

    @Override
    public SysUser findByUsername(String userName) {
        return users.stream().filter(o -> StringUtils.equals(o.getUserName(), userName)).findFirst().orElse(new SysUser());
    }
}
