package com.nesscomputing.config;

import org.junit.Assert;
import org.junit.Test;

public class TestConfig
{
    @Test
    public void testEmpty()
    {
        final Config emptyConfig = Config.getEmptyConfig();
        Assert.assertNotNull(emptyConfig);
        Assert.assertFalse(emptyConfig.getConfiguration().getKeys().hasNext());
    }
}