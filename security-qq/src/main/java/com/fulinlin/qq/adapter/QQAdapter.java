package com.fulinlin.qq.adapter;

import com.fulinlin.qq.api.QQ;
import com.fulinlin.qq.model.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * @program: spring-seruirty
 * @author: Fulin
 * @create: 2019-06-14 16:33
 **/
public class QQAdapter implements ApiAdapter<QQ> {
    /**
     * 测试qq认证服务器是否 还能用
     *
     * @param api
     * @return
     */
    @Override
    public boolean test(QQ api) {
        return true;
    }

    /**
     * 将qq个性化的数据
     * 转换为spring social标准的数据结构
     *
     * @param api
     * @param values
     */
    @Override
    public void setConnectionValues(QQ api, ConnectionValues values) {
        // 获取用户信息
        QQUserInfo userInfo = api.getUserInfo();
        // 显示的用户名字
        values.setDisplayName(userInfo.getNickname());
        // 用户头像
        values.setImageUrl(userInfo.getFigureurl_qq_1());
        // 个人主页
        values.setProfileUrl(null);
        // 服务提供商的用户id，即openId
        values.setProviderUserId(userInfo.getOpenId());
    }

    @Override
    public UserProfile fetchUserProfile(QQ api) {
        return null;
    }

    @Override
    public void updateStatus(QQ api, String message) {

    }
}
