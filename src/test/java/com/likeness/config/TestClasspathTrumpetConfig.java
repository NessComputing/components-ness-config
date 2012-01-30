package com.likeness.config;

import java.net.URI;

import org.junit.Before;

import com.likeness.config.Config;

public class TestClasspathTrumpetConfig extends AbstractTestConfig
{
    @Before
    public void setUp()
    {
        cfg = Config.getConfig(URI.create("classpath:/test-config/basic"), "values");
        cfg2 = Config.getConfig(URI.create("classpath:/test-config/basic-legacy"), "values");
    }
}


