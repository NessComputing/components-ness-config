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


import java.net.URI;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.likeness.config.Config;
import com.likeness.config.test.TestBean;

public class TestBeanValues
{
    private Config cfg = null;
    private Config cfg2 = null;

    @Before
    public void setUp()
    {
        cfg = Config.getConfig("classpath:/test-config/basic", "values");
        cfg2 = Config.getConfig("classpath:/test-config/basic-legacy", "values");
    }

    @After
    public void tearDown()
    {
        cfg = null;
        cfg2 = null;
    }

    @Test
    public void testBean()
    {
        Assert.assertThat(cfg, is(notNullValue()));

        final TestBean testBean = cfg.getBean(TestBean.class);

        Assert.assertThat(testBean.getStringValue(), is("the-test-value"));
        Assert.assertThat(testBean.getIntValue(), is(12345));
        Assert.assertThat(testBean.getBooleanValue(), is(true));
        Assert.assertThat(testBean.getUriValue(), is(URI.create("http://www.likeness.com/")));
    }

    @Test
    public void testBean2()
    {
        Assert.assertThat(cfg2, is(notNullValue()));

        final TestBean testBean = cfg2.getBean(TestBean.class);

        Assert.assertThat(testBean.getStringValue(), is("the-test-value"));
        Assert.assertThat(testBean.getIntValue(), is(12345));
        Assert.assertThat(testBean.getBooleanValue(), is(true));
        Assert.assertThat(testBean.getUriValue(), is(URI.create("http://www.likeness.com/")));
    }
}

