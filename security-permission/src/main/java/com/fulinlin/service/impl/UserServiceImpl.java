package com.fulinlin.service.impl;

import com.fulinlin.init.InitData;
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



    @Override
    public SysUser findByUsername(String userName) {
        return InitData.SYS_USERS.stream().filter(o -> StringUtils.equals(o.getUserName(), userName)).findFirst().orElse(null);
    }
}
