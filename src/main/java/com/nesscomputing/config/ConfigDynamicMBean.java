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

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.AbstractConfiguration;

import com.google.common.collect.Maps;

/**
 * Export all Config keys to JMX.
 */
class ConfigDynamicMBean extends AbstractDynamicMBean
{

    ConfigDynamicMBean(String name, Config config)
    {
        super (name, toMap(config));
    }

    private static Map<String, Object> toMap(Config config)
    {
        AbstractConfiguration configuration = config.getConfiguration();
        Iterator<String> keys = configuration.getKeys();

        Map<String, Object> result = Maps.newHashMap();
        while (keys.hasNext())
        {
            String key = keys.next();
            result.put(key, configuration.getString(key));
        }

        return result;
    }
}
