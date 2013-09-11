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

import java.beans.PropertyDescriptor;
import java.util.Map;

import com.google.common.collect.Maps;

import org.apache.commons.lang3.ObjectUtils;
import org.skife.config.cglib.core.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Export a single ConfigMagic bean to JMX
 */
class ConfigMagicDynamicMBean extends AbstractDynamicMBean
{
    private static final String CONFIG_MAGIC_CALLBACKS_NAME = "callbacks";
    private static final Logger LOG = LoggerFactory.getLogger(ConfigMagicDynamicMBean.class);

    ConfigMagicDynamicMBean(String name, Object configBean)
    {
        super(name, toMap(configBean));
    }

    private static Map<String, Object> toMap(Object configBean)
    {
        PropertyDescriptor[] props = ReflectUtils.getBeanGetters(configBean.getClass());

        Map<String, Object> result = Maps.newHashMap();

        for (PropertyDescriptor prop : props)
        {
            if (CONFIG_MAGIC_CALLBACKS_NAME.equals(prop.getName()))
            {
                continue;
            }

            try
            {
                result.put(prop.getName(), ObjectUtils.toString(prop.getReadMethod().invoke(configBean), null));
            } catch (Exception e)
            {
                LOG.error(String.format("For class %s, unable to find config property %s", configBean.getClass(), prop), e);
            }
        }

        return result;
    }
}
