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
package com.likeness.config;


import java.io.IOException;
import java.net.URI;

import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.junit.After;
import org.junit.Before;

import com.likeness.config.Config;
import com.likeness.config.util.LocalHttpService;

public class TestHttpConfig extends AbstractTestConfig
{
    private LocalHttpService localHttpService = null;

    @Before
    public void setUp() throws Exception
    {
        final ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setBaseResource(Resource.newClassPathResource("/"));
        localHttpService = LocalHttpService.forHandler(resourceHandler);
        localHttpService.start();

        final URI baseUri = new URI("http", null, localHttpService.getHost(), localHttpService.getPort(), null, null, null);

        cfg = Config.getConfig(baseUri.resolve("/test-config/basic"), "values");
        cfg2 = Config.getConfig(baseUri.resolve("/test-config/basic-legacy"), "values");
    }

    @After
    public void shutdownJetty() throws IOException
    {
        localHttpService.stop();
        localHttpService = null;
    }
}


