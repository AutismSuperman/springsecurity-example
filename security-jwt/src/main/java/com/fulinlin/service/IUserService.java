package com.fulinlin.service;

import com.fulinlin.pojo.User;

public interface IUserService {
    User findByUsername(String userName);
}
