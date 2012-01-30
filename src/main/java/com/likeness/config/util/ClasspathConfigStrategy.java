package com.likeness.config.util;

import com.likeness.config.Config;
import com.likeness.logging.Log;

import java.net.URI;
import java.net.URL;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ClasspathConfigStrategy extends AbstractConfigStrategy
{
    private static Log LOG = Log.findLog();

    public ClasspathConfigStrategy()
    {
        this(URI.create("classpath:/config"));
    }

    public ClasspathConfigStrategy(final URI configLocation)
    {
        super(configLocation);
        LOG.trace("Searching for configuration at '%s' on the classpath.", getLocation().getPath());
    }

    @Override
    public AbstractConfiguration load(final String configName, final String configPath)
        throws ConfigurationException
    {
        final String classpathPrefix = getLocation().getPath();

        // A property configuration lives in a configuration directory and is called
        // "config.properties"
        final String [] propertyFileNames = new String [] {
            classpathPrefix + "/" + configPath + "/config.properties",
            classpathPrefix + "/" + configName + ".properties"
        };

        for (String propertyFileName : propertyFileNames) {
            LOG.trace("Trying to load '%s'...", propertyFileName);
            final URL configUrl = Config.class.getResource(propertyFileName);
            if (configUrl != null) {
                LOG.trace("... succeeded");
                return new PropertiesConfiguration(configUrl);
            }
            else {
                LOG.trace("... failed");
            }
        }
        return null;
    }
}
