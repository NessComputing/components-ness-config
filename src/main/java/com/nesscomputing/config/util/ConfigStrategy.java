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

import java.net.URI;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.ConfigurationException;

public interface ConfigStrategy
{
    /**
     * Tries to load a configuration object.
     * @param configName The basic name of a configuration source. This is a configuration name such as "test" or "production".
     * @param configPath A full path to a configName. For a hierarchical configuration, this can be a path such as "production:db2" or "test:special".0
     * @return A Configuration object or null if none exists.
     * @throws ConfigurationException
     */
    AbstractConfiguration load(String configName, String configPath) throws ConfigurationException;

    URI getLocation();
}
