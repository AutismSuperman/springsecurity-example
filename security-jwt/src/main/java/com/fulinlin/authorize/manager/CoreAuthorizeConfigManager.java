package com.fulinlin.authorize.manager;

import com.fulinlin.authorize.Provider.AuthorizeConfigProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CoreAuthorizeConfigManager implements AuthorizeConfigManager {

    private final List<AuthorizeConfigProvider> authorizeConfigProviders;

    public CoreAuthorizeConfigManager(List<AuthorizeConfigProvider> authorizeConfigProviders) {
        this.authorizeConfigProviders = authorizeConfigProviders;
    }

    @Override
    public void config(HttpSecurity httpSecurity) throws Exception {
        for (AuthorizeConfigProvider provider : authorizeConfigProviders) {
            provider.config(httpSecurity);
        }
    }
}
