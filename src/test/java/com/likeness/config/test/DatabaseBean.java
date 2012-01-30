package com.likeness.config.test;

import org.skife.config.Config;

public abstract class DatabaseBean
{
    @Config("uri")
    public abstract String getDbUri();

    @Config("user")
    public abstract String getDbUser();

    @Config("password")
    public abstract String getDbPassword();
}
