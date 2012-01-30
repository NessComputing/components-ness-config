package com.likeness.config;


import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

import javax.annotation.Nullable;

/**
 * Provides an arbitrary configuration bean which is configured from the main Config object.
 *
 * @param <T> The type of the configuration bean.
 */
public class ConfigProvider<T> implements Provider<T>
{
	private final String prefix;
	private final Class<T> clazz;
	private final Map<String, String> overrides;

    private Config config = null;

    /**
     * Returns a Provider for a configuration bean. This method should be used in Modules
     * that require access to configuration.
     * @param <TYPE> The type of the Configuration bean.
     * @param clazz The class of the Configuration bean.
     * @return A provider.
     */
    public static final <TYPE> Provider<TYPE> of(final Class<TYPE> clazz)
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
    public static final <TYPE> Provider<TYPE> of(@Nullable final String prefix, final Class<TYPE> clazz)
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
    public static final <TYPE> Provider<TYPE> of(@Nullable final String prefix, final Class<TYPE> clazz,
    		@Nullable final Map<String, String> overrides)
    {
        return new ConfigProvider<TYPE>(prefix, clazz, overrides);
    }

    private ConfigProvider(final String prefix, final Class<T> clazz, final Map<String, String> overrides)
    {
    	this.prefix = prefix;
        this.clazz = clazz;
        this.overrides = overrides;
    }

    @Inject
    public void setInjector(final Injector injector)
    {
        this.config = injector.getInstance(Config.class);
    }

    @Override
    public T get()
    {
    	return config.getBean(prefix, clazz, overrides);
    }
}