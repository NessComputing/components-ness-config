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
import org.junit.Test;

import com.nesscomputing.config.Config;
import com.nesscomputing.config.util.LocalHttpService;
import com.nesscomputing.testing.lessio.AllowNetworkAccess;

@AllowNetworkAccess(endpoints={"127.0.0.1:*"})
public class TestHttpAuthTrumpetConfig
{
    public static final String LOGIN_USER = "demo";
    public static final String LOGIN_PW = "secret";

    private LocalHttpService localHttpService = null;

    protected Config cfg = null;

    @Before
    public void setUp() throws Exception
    {
        final ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setBaseResource(Resource.newClassPathResource("/"));

        localHttpService = LocalHttpService.forSecureHandler(resourceHandler, LOGIN_USER, LOGIN_PW);
        localHttpService.start();

    }

    @After
    public void shutdownJetty() throws IOException
    {
        localHttpService.stop();
        localHttpService = null;
        Assert.assertNull(cfg);
        cfg = null;
    }

    @Test(expected = IllegalStateException.class)
    public void loadStringValues() throws Exception
    {
        final URI baseUri = new URI("http", null, localHttpService.getHost(), localHttpService.getPort(), null, null, null);

        Config.getConfig(baseUri.resolve("/test-config/basic"), "values");
    }
}


