package com.likeness.config;


import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.likeness.config.Config;
import com.likeness.config.util.PropertiesSaver;

public class LegacyConfigTest
{
    @Before
    public void setUp()
    {
    }

    @Test public void testConfig() {
        Config p = Config.getConfig("classpath:/test-config/legacy", "production");
        Config t = Config.getConfig("classpath:/test-config/legacy", "test");

        Assert.assertTrue(p.getConfiguration().getBoolean("production"));
        Assert.assertFalse(t.getConfiguration().getBoolean("production"));
    }

    @Test
    public void testDefaultConfig()
    {
        Config d = Config.getConfig("classpath:/test-config/legacy", "default");

        Assert.assertFalse(d.getConfiguration().getBoolean("production"));
        Assert.assertTrue(d.getConfiguration().getBoolean("unitTesting", false));
    }

    @Test public void testSuperConfig() {
        Config ts = Config.getConfig("classpath:/test-config/legacy", "test:test_super");
        Configuration c = ts.getConfiguration();
        Assert.assertEquals("yay", c.getString("hi"));
        Assert.assertEquals("frob", c.getString("yay"));
    }

    @Test public void testEnvironment() {
    	PropertiesSaver ps = new PropertiesSaver(Config.CONFIG_PROPERTY_NAME, Config.CONFIG_LOCATION_PROPERTY_NAME);

    	try {
    		System.setProperty(Config.CONFIG_PROPERTY_NAME, "main/override");
    		System.setProperty(Config.CONFIG_LOCATION_PROPERTY_NAME, "classpath:/test-config/env");

    		Config legacy = Config.getConfig();
    		Configuration c = legacy.getConfiguration();
    		Assert.assertEquals("main-value", c.getString("main-key"));
            Assert.assertEquals("override-value", c.getString("override-key"));
            Assert.assertEquals("main-override-value", c.getString("main-override-key"));

            Assert.assertEquals("main/override", c.getString(Config.CONFIG_PROPERTY_NAME));

    	}
    	finally {
    		ps.apply();
    	}
    }
}
