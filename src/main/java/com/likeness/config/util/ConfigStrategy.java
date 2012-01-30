package com.likeness.config.util;

import java.net.URI;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.ConfigurationException;

public interface ConfigStrategy
{
	/**
	 * Tries to load a configuration object.
	 * @param configName The basic name of a configuration source. This is a configuration name such as "test" or "production".
	 * @param configPath A full path to a configName. For a hierarchical configuration, this can be a path such as "production:db2" or "test:special".0
	 * @return A Configuration object or null if none exists.
	 * @throws ConfigurationException
	 */
    AbstractConfiguration load(String configName, String configPath) throws ConfigurationException;

    URI getLocation();
}
