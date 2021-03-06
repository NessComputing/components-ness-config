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
package com.nesscomputing.config.util;

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
