package com.fulinlin.pojo;

import lombok.Data;

import java.util.List;

/**
 * @program: spring-seruirty
 * @author: Fulin
 * @create: 2019-05-13 16:15
 **/
@Data
public class SysUser {

    private Long id;
    private String userName;
    private String password;

    private List<String> roles;

    public SysUser() {
    }

    public SysUser(Long id, String userName, String password, List<String> roles) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }

}
