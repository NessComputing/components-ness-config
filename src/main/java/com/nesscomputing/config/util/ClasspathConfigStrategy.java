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
import com.nesscomputing.config.Config;

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
