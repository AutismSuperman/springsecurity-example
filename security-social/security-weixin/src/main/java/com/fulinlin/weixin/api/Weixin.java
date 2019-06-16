package com.fulinlin.weixin.api;

import com.fulinlin.weixin.model.WeixinUserInfo;

/**
 * 微信服务调取的接口
 * 这里主要为登陆做准备
 * 获取用户的信息
 */
public interface Weixin {

    WeixinUserInfo getUserInfo(String openId);
}

