package com.fulinlin.pojo;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @program: spring-seruirty
 * @author: Fulin
 * @create: 2019-05-13 16:15
 **/
@Data
public class SysUser implements Serializable {

    private Long id;
    private String userName;
    private String password;
    private List<SysRole> roles;


    public SysUser(Long id, String userName, String password, List<SysRole> roles) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }




}
