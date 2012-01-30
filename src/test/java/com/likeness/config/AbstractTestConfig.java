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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import org.apache.commons.configuration.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.likeness.config.Config;

public abstract class AbstractTestConfig
{
    protected Config cfg = null;
    protected Config cfg2 = null;

    @After
    public void tearDownConfig()
    {
        Assert.assertNotNull(cfg);
        Assert.assertNotNull(cfg2);
        cfg = null;
        cfg2 = null;
    }

    @Test
    public void loadStringValues()
    {
        Assert.assertThat(cfg, is(notNullValue()));
        final Configuration config = cfg.getConfiguration();

        Assert.assertThat(config, is(notNullValue()));

        final String s_cfg1 = config.getString("string-null-value");
        Assert.assertThat(s_cfg1, is(nullValue()));

        final String s_cfg2 = config.getString("string-value");
        Assert.assertThat(s_cfg2, is("the-test-value"));
    }

    @Test
    public void loadIntValues()
    {
        Assert.assertThat(cfg, is(notNullValue()));
        final Configuration config = cfg.getConfiguration();

        Assert.assertThat(config, is(notNullValue()));

        final Integer s_cfg1 = config.getInteger("int-null-value", null);
        Assert.assertThat(s_cfg1, is(nullValue()));

        final int s_cfg2 = config.getInt("int-value");
        Assert.assertThat(s_cfg2, is(12345));
    }

    @Test
    public void loadBooleanValues()
    {
        Assert.assertThat(cfg, is(notNullValue()));
        final Configuration config = cfg.getConfiguration();

        Assert.assertThat(config, is(notNullValue()));

        final Boolean s_cfg1 = config.getBoolean("boolean-null-value", null);
        Assert.assertThat(s_cfg1, is(nullValue()));

        final boolean s_cfg2 = config.getBoolean("boolean-value");
        Assert.assertThat(s_cfg2, is(true));
    }

    @Test
    public void loadUriValues()
    {
        Assert.assertThat(cfg, is(notNullValue()));
        final Configuration config = cfg.getConfiguration();

        Assert.assertThat(config, is(notNullValue()));

        final String s_cfg1 = config.getString("uri-null-value");
        Assert.assertThat(s_cfg1, is(nullValue()));

        final String s_cfg2 = config.getString("uri-value");
        Assert.assertThat(s_cfg2, is("http://www.likeness.com/"));
    }

    @Test
    public void loadStringValues2()
    {
        Assert.assertThat(cfg2, is(notNullValue()));
        final Configuration config = cfg2.getConfiguration();

        Assert.assertThat(config, is(notNullValue()));

        final String s_cfg1 = config.getString("string-null-value");
        Assert.assertThat(s_cfg1, is(nullValue()));

        final String s_cfg2 = config.getString("string-value");
        Assert.assertThat(s_cfg2, is("the-test-value"));
    }

    @Test
    public void loadIntValues2()
    {
        Assert.assertThat(cfg2, is(notNullValue()));
        final Configuration config = cfg2.getConfiguration();

        Assert.assertThat(config, is(notNullValue()));

        final Integer s_cfg1 = config.getInteger("int-null-value", null);
        Assert.assertThat(s_cfg1, is(nullValue()));

        final int s_cfg2 = config.getInt("int-value");
        Assert.assertThat(s_cfg2, is(12345));
    }

    @Test
    public void loadBooleanValues2()
    {
        Assert.assertThat(cfg2, is(notNullValue()));
        final Configuration config = cfg2.getConfiguration();

        Assert.assertThat(config, is(notNullValue()));

        final Boolean s_cfg1 = config.getBoolean("boolean-null-value", null);
        Assert.assertThat(s_cfg1, is(nullValue()));

        final boolean s_cfg2 = config.getBoolean("boolean-value");
        Assert.assertThat(s_cfg2, is(true));
    }

    @Test
    public void loadUriValues2()
    {
        Assert.assertThat(cfg2, is(notNullValue()));
        final Configuration config = cfg2.getConfiguration();

        Assert.assertThat(config, is(notNullValue()));

        final String s_cfg1 = config.getString("uri-null-value");
        Assert.assertThat(s_cfg1, is(nullValue()));

        final String s_cfg2 = config.getString("uri-value");
        Assert.assertThat(s_cfg2, is("http://www.likeness.com/"));
    }

}


