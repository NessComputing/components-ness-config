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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.nesscomputing.config.Config;
import com.nesscomputing.config.util.PropertiesSaver;

public class SystemOverrideTest
{
    private Config cfg = null;
    private Config cfg2 = null;

    private PropertiesSaver ps = null;

    @Before
    public void setUp()
    {
        ps = new PropertiesSaver("string-value");

        System.setProperty("string-value", "OVERRIDDEN");

        cfg = Config.getConfig("classpath:/test-config/basic", "values");
        Assert.assertThat(cfg.getSystemConfiguration().getString("string-value"), is("OVERRIDDEN"));

        cfg2 = Config.getConfig("classpath:/test-config//basic-legacy", "values");
        Assert.assertThat(cfg2.getSystemConfiguration().getString("string-value"), is("OVERRIDDEN"));
}

    @After
    public void tearDown()
    {
        cfg = null;
        cfg2 = null;

        Assert.assertNotNull(ps);
        ps.apply();
        ps = null;
    }

    @Test
    public void testDynamicSystemConfiguration()
    {
        System.clearProperty("string-value");

        Assert.assertThat(cfg.getSystemConfiguration().getString("string-value"), is(nullValue()));
        Assert.assertThat(cfg2.getSystemConfiguration().getString("string-value"), is(nullValue()));

        System.setProperty("string-value", "OVERRIDDEN");

        Assert.assertThat(cfg.getSystemConfiguration().getString("string-value"), is("OVERRIDDEN"));
        Assert.assertThat(cfg2.getSystemConfiguration().getString("string-value"), is("OVERRIDDEN"));
    }

    @Test
    public void loadStringValues()
    {
        Assert.assertThat(cfg, is(notNullValue()));
        final Configuration config = cfg.getConfiguration();

        Assert.assertThat(config, is(notNullValue()));

        Assert.assertThat(System.getProperty("string-value"), is("OVERRIDDEN"));
        Assert.assertThat(System.getProperties().getProperty("string-value"), is("OVERRIDDEN"));

        final String s_cfg2 = config.getString("string-value");
        Assert.assertThat(s_cfg2, is("OVERRIDDEN"));
    }

    @Test
    public void loadStringValues2()
    {
        Assert.assertThat(cfg2, is(notNullValue()));
        final Configuration config = cfg2.getConfiguration();

        Assert.assertThat(config, is(notNullValue()));

        Assert.assertThat(System.getProperty("string-value"), is("OVERRIDDEN"));

        final String s_cfg2 = config.getString("string-value");
        Assert.assertThat(s_cfg2, is("OVERRIDDEN"));
    }
}


