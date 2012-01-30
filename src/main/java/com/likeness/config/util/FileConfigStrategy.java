package com.likeness.config.util;

import com.likeness.logging.Log;

import java.io.File;
import java.net.URI;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class FileConfigStrategy extends AbstractConfigStrategy
{
    private static Log LOG = Log.findLog();

    private final File directoryLocation;

    public FileConfigStrategy(final URI directoryLocationUri)
    {
        super(directoryLocationUri);
        this.directoryLocation = new File(directoryLocationUri);
        LOG.trace("Searching for configuration at '%s'.", directoryLocation);
    }

    @Override
    public AbstractConfiguration load(final String configName, final String configPath)
        throws ConfigurationException
    {
        if (!(directoryLocation.exists() && directoryLocation.isDirectory() && directoryLocation.canRead())) {
            return null;
        }
        // A property configuration lives in a configuration directory and is called
        // "config.properties"
        final File [] propertyFiles = new File [] {
            new File(directoryLocation, configPath + File.separator + "config.properties"),
            new File(directoryLocation, configName + ".properties")
        };

        for (final File propertyFile : propertyFiles) {
            if (propertyFile.exists() && propertyFile.isFile() && propertyFile.canRead()) {
                LOG.trace("Trying to load '%s'...", propertyFile);
                try {
                    final AbstractConfiguration config = new PropertiesConfiguration(propertyFile);
                    LOG.trace("... succeeded");
                    return config;
                }
                catch (ConfigurationException ce) {
                    LOG.trace(ce, "... failed");
                }
            }
        }
        return null;
    }
}
