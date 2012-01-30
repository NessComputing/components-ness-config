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


