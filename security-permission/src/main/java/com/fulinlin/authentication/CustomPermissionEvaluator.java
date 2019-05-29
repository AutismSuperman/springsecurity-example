package com.fulinlin.authentication;

import com.fulinlin.init.InitData;
import com.fulinlin.pojo.SysRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {


    @Override
    public boolean hasPermission(Authentication authentication, Object targetRole, Object targetPermission) {
        //这里 targetUrl 可以做 url级别的权限
        // 获得loadUserByUsername()方法的结果
        User user = (User) authentication.getPrincipal();
        // 获得loadUserByUsername()中注入的角色
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        List<GrantedAuthority> collect = authorities.stream().filter(o -> StringUtils.equals(o.getAuthority(), targetRole.toString())).collect(Collectors.toList());
        // 遍历用户所有角色
        for (GrantedAuthority authority : collect) {
            String roleName = authority.getAuthority();
            SysRole sysRole = InitData.SYS_ROLES.stream().filter(o -> StringUtils.equals(o.getRoleName(), roleName)).findFirst().orElse(null);
            // 得到角色所有的权限
            List<String> permissions = new ArrayList<>();
            if (sysRole != null) {
                permissions = sysRole.getPermissions();
            }
            // 遍历权限集
            for (String p : permissions) {
                // 权限用户符合的话，返回true
                if (permissions.contains(targetPermission.toString())) {
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
