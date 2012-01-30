package com.likeness.config;


import com.google.inject.AbstractModule;

/**
 * Binding configuration to Guice. This is a pretty simple module right now but
 * it might grow over time.
 *
 * To get access to the System configuration, import this module.
 *
 */
public class ConfigModule extends AbstractModule
{
    private final Config config;

    public ConfigModule()
    {
        this.config = Config.getConfig();
    }

    public ConfigModule(final Config config)
    {
        this.config = config;
    }

    @Override
    public void configure()
    {
        bind(Config.class).toInstance(config);
    }

    protected Config getConfig()
    {
        return config;
    }
}
