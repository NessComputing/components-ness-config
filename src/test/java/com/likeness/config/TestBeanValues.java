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

