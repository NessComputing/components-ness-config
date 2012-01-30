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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.skife.config.CommonsConfigSource;
import org.skife.config.ConfigurationObjectFactory;

import com.likeness.logging.Log;
import com.likeness.tinyhttp.HttpContentConverter;
import com.likeness.tinyhttp.HttpFetcher;
import com.likeness.tinyhttp.ssl.SSLConfig;

public class HttpConfigStrategy extends AbstractConfigStrategy
{
    private static Log LOG = Log.findLog();

    private static final HttpContentConverter<AbstractConfiguration> CONFIG_CONVERTER = new AbstractConfigurationConverter();

    private final URI webLocation;

    public HttpConfigStrategy(@Nonnull final URI webLocation)
    {
        super(webLocation);
        final String web = webLocation.toString();
        if (web.endsWith("/")) {
            this.webLocation = webLocation;
        }
        else {
            this.webLocation = URI.create(web + "/");
        }
        LOG.trace("Searching for configuration at '%s'.", webLocation);
    }

    @Override
    public AbstractConfiguration load(final String configName, final String configPath)
        throws ConfigurationException
    {
        final ConfigurationObjectFactory objectFactory = new ConfigurationObjectFactory(new CommonsConfigSource(new SystemConfiguration().subset("config")));
        final SSLConfig sslConfig = objectFactory.build(SSLConfig.class);
        final HttpFetcher httpFetcher = new HttpFetcher(sslConfig);

        final ConfigAuthCredentials configAuthCredentials = objectFactory.build(ConfigAuthCredentials.class);
        try {
            // A property configuration lives in a configuration directory and is called
            // "config.properties"
            final URI [] propertyUris = new URI [] {
                webLocation.resolve(configPath + "/config.properties"),
                webLocation.resolve(configName + ".properties")
            };

            for (final URI propertyUri : propertyUris) {
                LOG.trace("Trying to load '%s'...", propertyUri);
                try {
                    final AbstractConfiguration config = httpFetcher.get(propertyUri, configAuthCredentials.getLogin(), configAuthCredentials.getPassword(), CONFIG_CONVERTER);
                    if (config != null) {
                        LOG.trace("... succeeded");
                        return config;
                    }
                    else {
                        LOG.trace("... not found");
                    }
                }
                catch (IOException ioe) {
                    LOG.trace(ioe, "... failed");
                }
            }
            return null;
        }
        finally {
            httpFetcher.close();
        }
    }

    private static final class AbstractConfigurationConverter implements HttpContentConverter<AbstractConfiguration>
    {
        @Override
        public AbstractConfiguration convert(final HttpRequest request, final HttpResponse response, final InputStream inputStream)
            throws IOException
        {
            switch (response.getStatusLine().getStatusCode())
            {
                case HttpServletResponse.SC_NOT_FOUND:
                    return null;
                case HttpServletResponse.SC_UNAUTHORIZED:
                    throw new IllegalStateException(String.format("Could not load configuration from '%s', not authorized!", request.getRequestLine().getUri()));
                case HttpServletResponse.SC_OK:
                    try {
                        final PropertiesConfiguration config = new PropertiesConfiguration();
                        config.load(inputStream);
                        return config;
                    }
                    catch (ConfigurationException ce) {
                        throw new IOException("Could not load configuration", ce);
                    }
                default:
                    throw new IOException("Could not load configuration from " + request.getRequestLine().getUri() + " (" + response.getStatusLine().getStatusCode() + ")");
            }
        }
    }
}
