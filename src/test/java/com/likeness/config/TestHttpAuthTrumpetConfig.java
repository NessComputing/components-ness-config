package com.likeness.config;


import java.io.IOException;
import java.net.URI;

import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.likeness.config.Config;
import com.likeness.config.util.LocalHttpService;

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


