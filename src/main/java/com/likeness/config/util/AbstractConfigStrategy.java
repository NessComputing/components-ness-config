package com.likeness.config.util;

import java.net.URI;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.ConfigurationException;

public abstract class AbstractConfigStrategy implements ConfigStrategy
{
    private final URI location;


    protected AbstractConfigStrategy(final URI location)
    {
        this.location = location;
    }

    @Override
    public final URI getLocation()
    {
        return location;
    }

    @Override
    public abstract AbstractConfiguration load(final String configName, final String configPath) throws ConfigurationException;
}
