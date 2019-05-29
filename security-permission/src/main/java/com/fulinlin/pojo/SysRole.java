package com.fulinlin.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: spring-seruirty
 * @author: Fulin
 * @create: 2019-05-13 16:15
 **/
@Data
public class SysRole implements Serializable {

    private Long id;
    private String roleName;

    private List<String> permissions;

    public SysRole(Long id, String roleName, List<String> permissions) {
        this.id = id;
        this.roleName = roleName;
        this.permissions = permissions;
    }


}
