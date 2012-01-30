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


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.likeness.config.Config;
import com.likeness.config.test.DatabaseBean;

public class TestSubConfiguration
{
    private Config cfg = null;

    @Before
    public void setUp()
    {
        cfg = Config.getConfig("classpath:/test-config/basic", "databases");
    }

    @After
    public void tearDown()
    {
        cfg = null;
    }

    @Test
    public void testBean()
    {
        Assert.assertThat(cfg, is(notNullValue()));

        final DatabaseBean db1 = cfg.getBean("db1", DatabaseBean.class);

        Assert.assertThat(db1.getDbUri(), is("jdbc://db1/db1"));
        Assert.assertThat(db1.getDbUser(), is("db1-user"));
        Assert.assertThat(db1.getDbPassword(), is("db1-password"));

        final DatabaseBean db2 = cfg.getBean("db2", DatabaseBean.class);

        Assert.assertThat(db2.getDbUri(), is("jdbc://db2/db2"));
        Assert.assertThat(db2.getDbUser(), is("db2-user"));
        Assert.assertThat(db2.getDbPassword(), is("db2-password"));

        final DatabaseBean db3 = cfg.getBean("db3", DatabaseBean.class);

        Assert.assertThat(db3.getDbUri(), is("jdbc://db3/db3"));
        Assert.assertThat(db3.getDbUser(), is("db3-user"));
        Assert.assertThat(db3.getDbPassword(), is("db3-password"));

    }
}

