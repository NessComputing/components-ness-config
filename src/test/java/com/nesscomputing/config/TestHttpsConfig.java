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


import java.io.IOException;
import java.net.URI;

import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import com.google.common.io.Resources;
import com.nesscomputing.config.util.LocalHttpService;
import com.nesscomputing.config.util.PropertiesSaver;
import com.nesscomputing.testing.lessio.AllowNetworkAccess;

@AllowNetworkAccess(endpoints={"127.0.0.1:*"})
public class TestHttpsConfig extends AbstractTestConfig
{
    private LocalHttpService localHttpService = null;

    public static final String USER_LOGIN ="verysecret";
    public static final String USER_PW = "evenmoresecret";

    private PropertiesSaver ps = null;

    @Before
    public void setUp() throws Exception
    {
        final ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setBaseResource(Resource.newClassPathResource("/"));
        localHttpService =  LocalHttpService.forSecureSSLHandler(resourceHandler, USER_LOGIN, USER_PW);
        localHttpService.start();

        ps = new PropertiesSaver("config.login", "config.password", "config.ssl.truststore", "config.ssl.truststore.password");

        System.setProperty("config.login", USER_LOGIN);
        System.setProperty("config.password", USER_PW);
        System.setProperty("config.ssl.truststore", Resources.getResource(this.getClass(), "/test-client-keystore.jks").toString());
        System.setProperty("config.ssl.truststore.password", "changeit");


        final URI baseUri = new URI("https", null, localHttpService.getHost(), localHttpService.getPort(), null, null, null);

        cfg = Config.getConfig(baseUri.resolve("/test-config/basic"), "values");
        cfg2 = Config.getConfig(baseUri.resolve("/test-config/basic-legacy"), "values");
    }

    @After
    public void shutdownJetty() throws IOException
    {
        localHttpService.stop();
        localHttpService = null;

        Assert.assertNotNull(ps);
        ps.apply();
        ps = null;
    }
}