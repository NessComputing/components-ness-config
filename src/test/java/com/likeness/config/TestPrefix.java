package com.likeness.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.likeness.config.Config;
import com.likeness.config.util.PropertiesSaver;

public class TestPrefix
{
    private Config cfg = null;

    @Before
    public void setUp()
    {
        cfg = Config.getConfig("classpath:/test-config/prefix", "default");
    }

    @After
    public void tearDown()
    {
        cfg = null;
    }

    @Test
    public void testSubConfig()
    {
        Assert.assertThat(cfg, is(notNullValue()));

        final Configuration config = cfg.getConfiguration("prefix.of.three");

        Assert.assertThat(config, is(notNullValue()));

        final String s_cfg1 = config.getString("string-null-value");
        Assert.assertThat(s_cfg1, is(nullValue()));

        final String s_cfg2 = config.getString("string-value");
        Assert.assertThat(s_cfg2, is("the-test-value"));
    }

    @Test
    public void testOverride()
    {
        Assert.assertThat(cfg, is(notNullValue()));

        PropertiesConfiguration pc = new PropertiesConfiguration();
        pc.setProperty("prefix.of.three.string-null-value", "NULL");
        pc.setProperty("prefix.of.three.string-value", "another test value");

        Config c2 = new Config(cfg, pc);

        final Configuration config = c2.getConfiguration("prefix.of.three");

        Assert.assertThat(config, is(notNullValue()));

        final String s_cfg1 = config.getString("string-null-value");
        Assert.assertThat(s_cfg1, is("NULL"));

        final String s_cfg2 = config.getString("string-value");
        Assert.assertThat(s_cfg2, is("another test value"));
    }

    @Test
    public void testSystemStillWins()
    {
        Assert.assertThat(cfg, is(notNullValue()));

        PropertiesConfiguration pc = new PropertiesConfiguration();
        pc.setProperty("prefix.of.three.string-null-value", "NULL");
        pc.setProperty("prefix.of.three.string-value", "another test value");

        PropertiesSaver ps = new PropertiesSaver("prefix.of.three.string-value");

        try {
            System.setProperty("prefix.of.three.string-value", "system-value");

            Config c2 = new Config(cfg, pc);

            final Configuration config = c2.getConfiguration("prefix.of.three");

            Assert.assertThat(config, is(notNullValue()));

            final String s_cfg2 = config.getString("string-value");
            Assert.assertThat(s_cfg2, is("system-value"));
        }
        finally {
            ps.apply();
        }
    }
}


