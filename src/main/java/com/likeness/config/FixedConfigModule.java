package com.likeness.config;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.configuration.MapConfiguration;
import org.apache.commons.configuration.SystemConfiguration;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;

/**
 * A module which binds a Config object whose properties are defined by code.  By default, the module is empty.
 * This module is intended for test code to provide a defined set of configuration properties to an unit test.
 */
public class FixedConfigModule extends AbstractModule {

    private final Config config;

    public FixedConfigModule() {
        this(Collections.<String, String>emptyMap());
    }

    public FixedConfigModule(Map<String, String> configOverrides) {
        final ImmutableMap<String, String> map = ImmutableMap.<String, String>builder().putAll(configOverrides).build();
        this.config = Config.getFixedConfig(new SystemConfiguration(), new MapConfiguration(map));
    }

    protected Config getConfig() {
        return config;
    }

    @Override
    protected void configure() {
        bind(Config.class).toInstance(config);
    }
}
