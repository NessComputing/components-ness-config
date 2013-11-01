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
package com.nesscomputing.config;

import java.net.URI;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.configuration.tree.OverrideCombiner;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nesscomputing.config.util.ClasspathConfigStrategy;
import com.nesscomputing.config.util.ConfigStrategy;
import com.nesscomputing.config.util.FileConfigStrategy;
import com.nesscomputing.config.util.HttpConfigStrategy;

class ConfigFactory
{
    private static final Logger LOG = LoggerFactory.getLogger(ConfigFactory.class);

    private static final Map<String, ? extends ConfigStrategyProvider> STRATEGY_PROVIDERS;

    private interface ConfigStrategyProvider
    {
        ConfigStrategy getStrategy(URI configLocation);
    }

    static {
        STRATEGY_PROVIDERS = ImmutableMap.of(
            "classpath", new ConfigStrategyProvider() {
                @Override
                public ConfigStrategy getStrategy(final URI configLocation) {
                    return new ClasspathConfigStrategy(configLocation);
                }
            },
            "file", new ConfigStrategyProvider() {
                @Override
                public ConfigStrategy getStrategy(final URI configLocation) {
                    return new FileConfigStrategy(configLocation);
                }
            },
            "http", new ConfigStrategyProvider() {
                @Override
                public ConfigStrategy getStrategy(final URI configLocation) {
                    return new HttpConfigStrategy(configLocation);
                }
            },
            "https", new ConfigStrategyProvider() {
                @Override
                public ConfigStrategy getStrategy(final URI configLocation) {
                    return new HttpConfigStrategy(configLocation);
                }
            }
        );
    }

    private final String configName;
    private final ConfigStrategy configStrategy;

    ConfigFactory(@Nonnull final URI configLocation, @Nullable final String configName)
    {
        this.configName = Objects.firstNonNull(configName, "default");
        this.configStrategy = selectConfigStrategy(configLocation);
    }

    CombinedConfiguration load()
    {
        // Allow foo/bar/baz and foo:bar:baz
        final String [] configNames = StringUtils.stripAll(StringUtils.split(configName, "/:"));

        final CombinedConfiguration cc = new CombinedConfiguration(new OverrideCombiner());

        // All properties can be overridden by the System properties.
        cc.addConfiguration(new SystemConfiguration(), "systemProperties");

        boolean loadedConfig = false;
        for (int i = 0; i < configNames.length; i++) {
            final String configFileName = configNames[configNames.length - i - 1];
            final String configFilePath = StringUtils.join(configNames, "/", 0, configNames.length - i);

            try {
                final AbstractConfiguration subConfig = configStrategy.load(configFileName, configFilePath);
                if (subConfig == null) {
                    LOG.debug("Configuration '%s' does not exist, skipping", configFileName);
                }
                else {
                    cc.addConfiguration(subConfig, configFileName);
                    loadedConfig = true;
                }
            } catch (ConfigurationException ce) {
                LOG.error(String.format("While loading configuration '%s'", configFileName), ce);
            }
        }

        if (!loadedConfig && configNames.length > 0) {
            LOG.warn("Config name '%s' was given but no config file could be found, this looks fishy!", configName);
        }

        return cc;
    }

    private ConfigStrategy selectConfigStrategy(final URI configLocation)
    {
        final ConfigStrategyProvider configStrategyProvider = STRATEGY_PROVIDERS.get(configLocation.getScheme());
        if (configStrategyProvider == null) {
            throw new IllegalStateException("No strategy for " + configLocation + " available!");
        }

        return configStrategyProvider.getStrategy(configLocation);
    }
}
