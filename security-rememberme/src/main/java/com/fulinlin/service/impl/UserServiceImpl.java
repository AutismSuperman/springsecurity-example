package com.fulinlin.service.impl;

import com.fulinlin.pojo.User;
import com.fulinlin.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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

    private static final Set<User> users = new HashSet<>();

    static {
        users.add(new User(1L, "fulin", "1dc568b64c0f67e7a86c89a12fa5bd5f", Arrays.asList("admin", "docker")));
        users.add(new User(1L, "xiaohan", "1dc568b64c0f67e7a86c89a12fa5bd5f", Arrays.asList("admin", "docker")));
        users.add(new User(1L, "longlong", "1dc568b64c0f67e7a86c89a12fa5bd5f", Arrays.asList("admin", "docker")));
    }

    @Override
    public User findByUsername(String userName) {

        return users.stream().filter(o -> StringUtils.equals(o.getUsername(), userName)).findFirst().get();
    }
}
