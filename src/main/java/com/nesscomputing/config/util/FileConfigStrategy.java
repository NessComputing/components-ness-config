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
package com.nesscomputing.config.util;

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
