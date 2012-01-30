package com.likeness.config;

import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.likeness.config.Config;

public class HierarchyConfigTest
{
    @Before
    public void setUp()
    {
    }

    @Test public void testConfig() {
        Config p = Config.getConfig("classpath:/test-config/hierarchy", "production");
        Config t = Config.getConfig("classpath:/test-config/hierarchy", "test");

        Assert.assertTrue(p.getConfiguration().getBoolean("production"));

        Assert.assertFalse(t.getConfiguration().getBoolean("production"));
    }

    @Test
    public void testDefaultConfig()
    {
        Config d = Config.getConfig("classpath:/test-config/hierarchy", "default");

        Assert.assertFalse(d.getConfiguration().getBoolean("production"));

        Assert.assertTrue(d.getConfiguration().getBoolean("unitTesting", false));
    }

    @Test public void testSuperConfig() {
        Config ts = Config.getConfig("classpath:/test-config/hierarchy", "test:test_super");
        Configuration c = ts.getConfiguration();
        Assert.assertEquals("yay", c.getString("hi"));
        Assert.assertEquals("frob", c.getString("yay"));
    }
}
