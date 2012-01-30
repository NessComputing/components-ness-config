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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.nesscomputing.config.Config;
import com.nesscomputing.config.util.PropertiesSaver;

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


