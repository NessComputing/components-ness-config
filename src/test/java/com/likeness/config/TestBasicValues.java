package com.likeness.config;

import org.junit.Before;

import com.likeness.config.Config;

public class TestBasicValues extends AbstractTestConfig
{
    @Before
    public void setUp()
    {
        cfg = Config.getConfig("classpath:/test-config/basic", "values");
        cfg2 = Config.getConfig("classpath:/test-config/basic-legacy", "values");
    }
}


