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

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.skife.config.Default;
import org.weakref.jmx.testing.TestingMBeanServer;

public class TestConfigProvider
{
    public interface MyBean
    {
        @org.skife.config.Config("test.x")
        @Default("something")
        String getX();
    }

    private Config config;
    private MBeanServer server;
    private ConfigJmxExporter exporter;
    private ConfigProvider<MyBean> provider;

    @Before
    public void setUp()
    {
        config = Config.getEmptyConfig();
        server = new TestingMBeanServer();
        exporter = new ConfigJmxExporter(config);
        exporter.setMBeanServer(server);

        provider = (ConfigProvider<MyBean>) ConfigProvider.of(MyBean.class);
    }

    @Test
    public void testConfigThenExport() throws Exception
    {
        provider.setConfig(config);
        provider.setExporter(exporter);

        ObjectName beanName = new ObjectName("com.nesscomputing.config:n0=com,n1=nesscomputing,n2=config,n3=TestConfigProvider$MyBean");
        Assert.assertEquals("something", server.getAttribute(beanName, "x"));
    }

    @Test
    public void testExportThenConfig() throws Exception
    {
        provider.setExporter(exporter);
        provider.setConfig(config);

        ObjectName beanName = new ObjectName("com.nesscomputing.config:n0=com,n1=nesscomputing,n2=config,n3=TestConfigProvider$MyBean");
        Assert.assertEquals("something", server.getAttribute(beanName, "x"));
    }
}


