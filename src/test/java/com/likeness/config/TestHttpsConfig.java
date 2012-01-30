package com.likeness.config;


import java.io.IOException;
import java.net.URI;

import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import com.google.common.io.Resources;
import com.likeness.config.util.LocalHttpService;
import com.likeness.config.util.PropertiesSaver;

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