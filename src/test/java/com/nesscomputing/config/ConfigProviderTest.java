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
