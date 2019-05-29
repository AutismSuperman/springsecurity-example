package com.fulinlin.service;

import com.fulinlin.pojo.SysRole;
import com.fulinlin.pojo.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final IUserService iUserService;

    public UserService(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        SysUser sysUser = iUserService.findByUsername(s);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        //模拟从数据库获取角色权限
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        List<SysRole> roles = sysUser.getRoles();
        for (SysRole role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return new User(sysUser.getUserName(), sysUser.getPassword(), authorities);
    }

}
