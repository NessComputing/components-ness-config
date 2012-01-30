/**
 * Copyright (C) 2012 Ness Computing, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
