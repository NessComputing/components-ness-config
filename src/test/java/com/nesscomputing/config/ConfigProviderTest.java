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

import junit.framework.Assert;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class ConfigProviderTest {

    interface TestConfig {

    }

    @Test
    public void testConfigBeansSingleton() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                install (ConfigModule.forTesting());

                bind (TestConfig.class).toProvider(ConfigProvider.of(TestConfig.class));
            }
        });

        TestConfig first = injector.getInstance(TestConfig.class);
        TestConfig second = injector.getInstance(TestConfig.class);

        Assert.assertSame("not bound as singleton,", first, second);
    }
}
