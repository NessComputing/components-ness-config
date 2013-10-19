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


import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Provides an arbitrary configuration bean which is configured from the main Config object.
 *
 * @param <T> The type of the configuration bean.
 */
public final class ConfigProvider<T> implements Provider<T>
{
    private final String prefix;
    private final Class<T> clazz;
    private final Map<String, String> overrides;

    private volatile T configBean = null;
    private volatile ConfigJmxExporter exporter;

    /**
     * Returns a Provider for a configuration bean. This method should be used in Modules
     * that require access to configuration.
     * @param <TYPE> The type of the Configuration bean.
     * @param clazz The class of the Configuration bean.
     * @return A provider.
     */
    public static <TYPE> Provider<TYPE> of(final Class<TYPE> clazz)
    {
        return new ConfigProvider<TYPE>(null, clazz, null);
    }

    /**
     * Returns a Provider for a configuration bean. This method should be used in Modules
     * that require access to configuration.
     * @param <TYPE> The type of the Configuration bean.
     * @param prefix The Config bean prefix, as referenced below (may be null)
     * @param clazz The class of the Configuration bean.
     * @return A provider.
     * @see Config#getBean(String, Class)
     */
    public static <TYPE> Provider<TYPE> of(@Nullable final String prefix, final Class<TYPE> clazz)
    {
        return new ConfigProvider<TYPE>(prefix, clazz, null);
    }

    /**
     * Returns a Provider for a configuration bean. This method should be used in Modules
     * that require access to configuration.
     * @param <TYPE> The type of the Configuration bean.
     * @param prefix The Config bean prefix, as referenced below (may be null)
     * @param clazz The class of the Configuration bean.
     * @return A provider.
     * @see Config#getBean(String, Class)
     */
    public static <TYPE> Provider<TYPE> of(@Nullable final String prefix, final Class<TYPE> clazz,
            @Nullable final Map<String, String> overrides)
    {
        return new ConfigProvider<TYPE>(prefix, clazz, overrides);
    }

    /**
     * Returns a Provider for a configuration bean. This method should be used in Modules
     * that require access to configuration.
     * @param clazz The class of the Configuration bean.
     * @return A provider.
     * @see Config#getBean(String, Class)
     */
    public static <TYPE> Provider<TYPE> of(final Class<TYPE> clazz,
            @Nullable final Map<String, String> overrides)
    {
        return of(null, clazz, overrides);
    }

    private ConfigProvider(final String prefix, final Class<T> clazz, final Map<String, String> overrides)
    {
        this.prefix = prefix;
        this.clazz = clazz;
        this.overrides = overrides;
    }

    @Inject
    public void setConfig(final Config config)
    {
        this.configBean = config.getBean(prefix, clazz, overrides);
        tryExport();
    }

    @Inject(optional=true)
    void setExporter(final ConfigJmxExporter exporter)
    {
        this.exporter = exporter;
        tryExport();
    }

    private void tryExport()
    {
        if (exporter != null && configBean != null) {
            exporter.export(clazz, configBean);
        }
    }

    @Override
    public T get()
    {
        Preconditions.checkState(configBean != null, "configuration was never injected");
        return configBean;
    }
}
