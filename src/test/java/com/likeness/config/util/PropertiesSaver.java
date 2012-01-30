package com.likeness.config.util;

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
