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

import static org.hamcrest.CoreMatchers.is;


import org.apache.commons.configuration.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.likeness.config.Config;
import com.likeness.config.util.PropertiesSaver;

public class SystemPropertiesTest
{
    private Config cfg = null;

    private PropertiesSaver ps = null;

    @Before
    public void setUp()
    {
        ps = new PropertiesSaver(Config.CONFIG_PROPERTY_NAME, Config.CONFIG_LOCATION_PROPERTY_NAME);

        System.setProperty(Config.CONFIG_PROPERTY_NAME, "production:webautostart:db2");
        System.setProperty(Config.CONFIG_LOCATION_PROPERTY_NAME, "classpath:/test-config/legacy");
        cfg = Config.getConfig();
    }

    @After
    public void tearDown()
    {
        cfg = null;

        Assert.assertNotNull(ps);
        ps.apply();
        ps = null;
    }

    @Test
    public void testDB2Configuration()
    {
        Assert.assertThat(cfg.getConfiguration().getBoolean("production", false), is(true));

        Configuration config = cfg.getConfiguration();

        Assert.assertThat(config.getString("t_s_d"), is("t_s"));
        Assert.assertThat(config.getInt("c.m_s"), is(2));

        Assert.assertThat(config.getBoolean("b.a"), is(true));
        Assert.assertThat(config.getBoolean("y.a"), is(false));

        Assert.assertThat(config.getString("s.f-l.u"), is("zzz"));
        Assert.assertThat(config.getBoolean("c.c.a"), is(true));
    }
}


