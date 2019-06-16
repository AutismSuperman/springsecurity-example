package com.fulinlin.social.support;

import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.core.env.Environment;
import org.springframework.social.connect.ConnectionFactory;

public abstract class SocialFactoryAdapter extends SocialConfigurerAdapter {
    public SocialFactoryAdapter() {
    }

    public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment) {
        configurer.addConnectionFactory(this.createConnectionFactory());
    }

    protected abstract ConnectionFactory<?> createConnectionFactory();
}