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
import org.skife.config.TimeSpan;
import org.weakref.jmx.testing.TestingMBeanServer;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;

public class LateJmxExporterTest
{
    public interface MyBean
    {
        @org.skife.config.Config("test.x")
        String getX();

        @org.skife.config.Config("test.y")
        @Default("3")
        int getY();

        @org.skife.config.Config("test.z")
        TimeSpan getZ();
    }

    MBeanServer server = new TestingMBeanServer();

    @Before
    public void setUp()
    {
        Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure()
            {
                binder().requireExplicitBindings();

                install (ConfigModule.forTesting("test.x", "foo", "test.z", "10s"));
                bind (MyBean.class).toProvider(ConfigProvider.of(MyBean.class));
            }
        }).injectMembers(this);
    }

    @Inject
    ConfigJmxExporter exporter;


    @Test
    public void doTest() throws Exception
    {
        exporter.setMBeanServer(server);

        ObjectName beanName = new ObjectName("com.nesscomputing.config:n0=com,n1=nesscomputing,n2=config,n3=LateJmxExporterTest$MyBean");
        Assert.assertEquals("foo", server.getAttribute(beanName, "x"));
        Assert.assertEquals("3", server.getAttribute(beanName, "y"));
        Assert.assertEquals("10s", server.getAttribute(beanName, "z"));
    }
}
