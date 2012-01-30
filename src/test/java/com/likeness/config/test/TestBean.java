package com.likeness.config.test;

import java.net.URI;

import org.skife.config.Config;
import org.skife.config.Default;

public abstract class TestBean
{
    @Config("string-value")
    public abstract String getStringValue();

    @Config("int-value")
    @Default("0")
    public abstract int getIntValue();

    @Config("boolean-value")
    @Default("false")
    public abstract boolean getBooleanValue();

    @Config("uri-value")
    public abstract String internalUriValue();

    public URI getUriValue()
    {
        return URI.create(internalUriValue());
    }
}
