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

import java.util.HashMap;
import java.util.Map;

/**
 * Allows saving and restoring System.properties state, e.g. to modify it during an unit test.
 * The nature of System properties implies that changing these values will affect other threads.
 *
 * @author henning
 */
public class PropertiesSaver
{
    private final Map<String, String> properties = new HashMap<String, String>();

    // Factored out the two default config keys from Config to avoid dependency.
    private static final String [] DEFAULT_KEYS = new String [] {
        "TrumpetProduction",
        "TrumpetConfig"
    };

    /**
     * Record the state of some default properties (TrumpetProduction and TrumpetConfig).
     */
    public PropertiesSaver()
    {
        this(DEFAULT_KEYS);
    }

    /**
     * Record the state of the properties given.
     *
     * @param props Varargs list of property names.
     */
    public PropertiesSaver(final String ... props)
    {
        for (String property: props) {
            properties.put(property, System.getProperty(property));
        }
    }

    /**
     * Restore the state of the properties saved at construction time.
     */
    public synchronized void apply()
    {
        for (final Map.Entry<String, String> entry : properties.entrySet()) {
            if (entry.getValue() == null) {
                System.clearProperty(entry.getKey());
            }
            else {
                System.setProperty(entry.getKey(), entry.getValue());
            }
        }
    }
}
