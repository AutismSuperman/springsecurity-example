package com.fulinlin.service;

import com.fulinlin.pojo.SysUser;

public interface IUserService {
    SysUser findByUsername(String userName);
}
