package com.fulinlin.custom;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @program: SpringSecurity
 * @author: fulin
 * @create: 2019-03-28 19:21
 * <p>
 * <p>
 * 这个类先废弃这中动态权限不太好使
 * @Bean public AccessDecisionManager accessDecisionManager() {
 * List<AccessDecisionVoter<?>> decisionVoters
 * = Arrays.asList(
 * new WebExpressionVoter(),
 * new RoleBasedVoter(),
 * new AuthenticatedVoter(),
 * );
 * return new UnanimousBased(decisionVoters);
 * }
 * <p>
 * <p>
 * 配置链中加入
 * .authorizeRequests()// 对请求授权
 * .accessDecisionManager(accessDecisionManager())
 * <p>
 * 这个投票器中 object可以拿到url  然后你自己去数据库查询响应的权限在这
 * 怎么进行扩展都可以
 **/
//@Deprecated
public class MyDynamicVoter implements AccessDecisionVoter<Object> {
    /**
     * supports 方法说明这个投票器是否可以传递到下一个投票器
     * 可以支持传递，则返回true
     *
     * @param attribute
     * @return
     */
    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        //如果没有进行认证则永远返回 -1
        if (authentication == null) {
            return ACCESS_DENIED;
        }
        int result = ACCESS_ABSTAIN;
        Collection<? extends GrantedAuthority> authorities = extractAuthorities(authentication);
        for (ConfigAttribute attribute : attributes) {
            if (attribute.getAttribute() == null) {
                continue;
            }
            if (this.supports(attribute)) {
                result = ACCESS_DENIED;
                for (GrantedAuthority authority : authorities) {
                    if (attribute.getAttribute().equals(authority.getAuthority())) {
                        return ACCESS_GRANTED;
                    }
                }
            }
        }
        return result;
    }

    private Collection<? extends GrantedAuthority> extractAuthorities(
            Authentication authentication) {
        return authentication.getAuthorities();
    }

}
