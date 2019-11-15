package com.fulinlin.custom;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: SpringSecurity
 * @author: fulin
 * @create: 2019-11-10 22:56
 **/
public class DynamicFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {


    private FilterInvocationSecurityMetadataSource superMetadataSource;


    public DynamicFilterInvocationSecurityMetadataSource(FilterInvocationSecurityMetadataSource expressionBasedFilterInvocationSecurityMetadataSource) {
        this.superMetadataSource = expressionBasedFilterInvocationSecurityMetadataSource;
    }

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final Map<String, String> urlRoleMap = new HashMap<String, String>() {{
        put("/tt", "ROLE_JAVA1");
    }};

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();
        for (Map.Entry<String, String> entry : urlRoleMap.entrySet()) {
            if (antPathMatcher.match(entry.getKey(), url)) {
                return SecurityConfig.createList(entry.getValue());
            }
        }
        //  返回代码定义的默认配置
        return superMetadataSource.getAttributes(object);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
