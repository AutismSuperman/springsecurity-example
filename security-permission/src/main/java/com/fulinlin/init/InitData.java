package com.fulinlin.init;

import com.fulinlin.pojo.SysRole;
import com.fulinlin.pojo.SysUser;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: spring-seruirty
 * @author: Fulin
 * @create: 2019-05-29 17:41
 **/
public class InitData {


    public static final Set<SysUser> SYS_USERS = new HashSet<>();

    public static final Set<SysRole> SYS_ROLES = new HashSet<>();

    static {
        SYS_ROLES.add(new SysRole(1L, "ROLE_JAVA", Arrays.asList("c", "r", "u", "d")));
        SYS_ROLES.add(new SysRole(2L, "ROLE_DOCKER", Arrays.asList("c", "r", "u")));
        SYS_ROLES.add(new SysRole(3L, "ROLE_PHP", Arrays.asList("c", "u")));
        SYS_ROLES.add(new SysRole(4L, "ROLE_PYTHON", Arrays.asList("c", "d")));
        SYS_ROLES.add(new SysRole(5L, "ROLE_CENTOS", Arrays.asList("c", "r", "d")));

    }


    static {
        SYS_USERS.add(
                new SysUser(1L, "fulin", "123456",
                        SYS_ROLES.stream().filter(o -> StringUtils.equalsAny(o.getRoleName(), "ROLE_JAVA", "ROLE_DOCKER")).collect(Collectors.toList())
                )
        );
        SYS_USERS.add(
                new SysUser(2L, "maoxiansheng", "123456",
                        SYS_ROLES.stream().filter(o -> StringUtils.equalsAny(o.getRoleName(), "ROLE_PHP", "ROLE_DOCKER")).collect(Collectors.toList())
                )
        );
        SYS_USERS.add(
                new SysUser(3L, "happy fish", "123456",
                        SYS_ROLES.stream().filter(o -> StringUtils.equalsAny(o.getRoleName(), "ROLE_PYTHON", "ROLE_CENTOS")).collect(Collectors.toList())
                )
        );
    }


}
