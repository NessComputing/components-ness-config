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
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.MapConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.skife.config.CommonsConfigSource;
import org.skife.config.ConfigurationObjectFactory;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.nesscomputing.config.util.ImmutableConfiguration;
import com.nesscomputing.logging.Log;

/**
 * Load configurations from a hierarchy of configuration files. A hierarchy is defined as "a/b/c/d/..." more local
 * configurations being further right. A property defined in a more local configuration overrides a more global
 * definition.
 *
 * Configuration is loaded either as "<name>.properties" files or from "<name>/config.properties". All configuration
 * is loaded relative to a "configuration location".
 *
 * A configuration location can be given using the explicit constructor or through environment variables (ness.config and ness.config.location).
 */
public final class Config
{
    private static final Log LOG = Log.findLog();

    /** Java system property for setting the configuration. */
    public static final String CONFIG_PROPERTY_NAME = "ness.config";
    public static final String CONFIG_LOCATION_PROPERTY_NAME = "ness.config.location";

    private static final Object NULL_OBJECT = new Object();
    private ConcurrentMap<Object, ConfigurationObjectFactory> objectFactories = Maps.newConcurrentMap();

    private final CombinedConfiguration config;

    /**
     * Creates a fixed configuration for the supplied {@link AbstractConfiguration} objects. Only key/value
     * pairs from these objects will be present in the final configuration. There is no implicit override from
     * system properties.
     */
    public static Config getFixedConfig(@Nullable final AbstractConfiguration ... configs)
    {
        final CombinedConfiguration cc = new CombinedConfiguration();

        if (configs != null) {
            for (final AbstractConfiguration config : configs) {
                cc.addConfiguration(config);
            }
        }
        return new Config(cc);
    }

    /**
     * Creates a fixed configuration for the supplied key/value pairs. The number of elements passed in must be an
     * even number of elements, otherwise the last one is ignored.
     *
     * Config objects created from this method will not accept overrides from system properties.
     */
    public static Config getFixedConfig(final String ... keyValuePairs)
    {
        final Map<String, String> properties = Maps.newHashMap();
        if (keyValuePairs != null) {
            for(final Iterator<String> it = Arrays.asList(keyValuePairs).iterator(); it.hasNext();) {
                final String key = it.next();
                if (it.hasNext()) {
                    properties.put(key, it.next());
                }
                else {
                    LOG.warn("Found an odd number of arguments for key/value pairs. Ignoring key %s", key);
                    break;
                }
            }
        }
        return getFixedConfig(new MapConfiguration(properties));
    }

    /**
     * Loads the configuration. The no-args method uses system properties to determine which configurations
     * to load.
     *
     * -Dness.config=x/y/z defines a hierarchy of configurations from general
     * to detail.
     *
     * If the ness.config.location variable is unset, the config is empty.
     * If the ness.config variable is unset, the default value "default" is used.
     */
    public static Config getConfig()
    {
        final Configuration systemConfig = new SystemConfiguration();
        final String configName = systemConfig.getString(CONFIG_PROPERTY_NAME);
        final String configLocation = systemConfig.getString(CONFIG_LOCATION_PROPERTY_NAME);
        Preconditions.checkState(configLocation != null, "Config location must be set!");
        final ConfigFactory configFactory = new ConfigFactory(URI.create(configLocation), configName);
        return new Config(configFactory.load());
    }

    /**
     * Load system configuration, using the supplied URI as base.
     */
    public static Config getConfig(@Nonnull final URI configLocation, @Nullable final String configName)
    {
        final ConfigFactory configFactory = new ConfigFactory(configLocation, configName);
        return new Config(configFactory.load());
    }

    /**
     * Load system configuration, using the supplied configLocation as base. The config location is converted
     * into an URI first.
     */
    public static Config getConfig(@Nonnull final String configLocation, @Nullable final String configName)
    {
        final ConfigFactory configFactory = new ConfigFactory(URI.create(configLocation), configName);
        return new Config(configFactory.load());
    }

    private Config(@Nonnull final CombinedConfiguration config)
    {
        this.config = config;
    }

    public Config(final Config config, final AbstractConfiguration ... overrideConfigurations)
    {
        final CombinedConfiguration baseConfig = config.config;

        if (overrideConfigurations == null || overrideConfigurations.length == 0) {
            this.config = baseConfig;
        }
        else {
            final CombinedConfiguration cc = new CombinedConfiguration();

            final AbstractConfiguration first = AbstractConfiguration.class.cast(baseConfig.getConfiguration(0)); // cast always succeeds, internally this returns cd.getConfiguration() which is AbstractConfiguration
            Preconditions.checkState(first != null && first.getClass() == SystemConfiguration.class, "First config in parent is not the SystemConfiguration, you are in trouble...");
            cc.addConfiguration(first);

            for (AbstractConfiguration c : overrideConfigurations) {
                cc.addConfiguration(c);
            }

            for (int i = 1; i < baseConfig.getNumberOfConfigurations(); i++) {
                cc.addConfiguration((AbstractConfiguration) baseConfig.getConfiguration(i));
            }

            this.config = cc;
        }
    }

    public AbstractConfiguration getConfiguration()
    {
        return new ImmutableConfiguration(config);
    }

    public AbstractConfiguration getConfiguration(final String prefix)
    {
        return new ImmutableConfiguration(config.subset(prefix));
    }

    public AbstractConfiguration getSystemConfiguration()
    {
        return new ImmutableConfiguration(new SystemConfiguration());
    }

    public <T> T getBean(Class<T> classType)
    {
        return getBean(null, classType, null);
    }

    public <T> T getBean(final String prefix, final Class<T> classType)
    {
        return getBean(prefix, classType, null);
    }

    public <T> T getBean(final Class<T> classType, final Map<String, String> replacements)
    {
        return getBean(null, classType, replacements);
    }

    public <T> T getBean(final String prefix, final Class<T> classType, final Map<String, String> replacements)
    {
        ConfigurationObjectFactory factory = null;
        Object key = Objects.firstNonNull(prefix, NULL_OBJECT);
        factory = objectFactories.get(key);
        if (factory == null) {
            Configuration cfg = prefix == null ? config : config.subset(prefix);
            factory = new ConfigurationObjectFactory(new CommonsConfigSource(cfg));
            ConfigurationObjectFactory newFactory = objectFactories.putIfAbsent(key, factory);
            factory = Objects.firstNonNull(newFactory, factory);
        }
        return factory.buildWithReplacements(classType, replacements);
    }

    private transient String toStringValue = null;

    @Override
    public String toString()
    {
        if (config == null) {
            return "<uninitialized>";
        }
        if (toStringValue == null) {
            StringBuilder sb = new StringBuilder("[");
            for (Iterator<?> it = config.getKeys(); it.hasNext(); ) {
                String key = (String) it.next();
                sb.append(key);
                sb.append("->");
                sb.append(config.getString(key));

                if (it.hasNext()) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            toStringValue = sb.toString();
        }
        return toStringValue;
    }
}
