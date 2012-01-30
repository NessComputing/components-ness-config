package com.likeness.config.util;

import javax.annotation.Nullable;

import org.skife.config.Config;
import org.skife.config.DefaultNull;

public abstract class ConfigAuthCredentials
{
    @Config("login")
    @DefaultNull
    @Nullable
    public String getLogin()
    {
        return null;
    }

    @Config("password")
    @DefaultNull
    @Nullable
    public String getPassword()
    {
        return null;
    }
}
