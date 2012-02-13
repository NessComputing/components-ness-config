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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;
import com.nesscomputing.config.Config;
import com.nesscomputing.testing.lessio.AllowLocalFileAccess;

@AllowLocalFileAccess(paths={"*/test-config/*","%TMP_DIR%"})
public class TestFileTrumpetConfig extends AbstractTestConfig
{
    File dir = null;

    @Before
    public void setUp() throws IOException
    {
        final String [] fileNames = {
              "/test-config/basic/default/override/config.properties",
              "/test-config/basic/default/config.properties",
              "/test-config/basic/values/config.properties",
              "/test-config/basic/databases.properties",
              "/test-config/basic-legacy/default.properties",
              "/test-config/basic-legacy/override.properties",
              "/test-config/basic-legacy/values.properties"
        };

        Assert.assertNull(dir);
        dir = Files.createTempDir();

        for(String fileName : fileNames) {
            final InputSupplier<InputStream> input = Resources.newInputStreamSupplier(Resources.getResource(Config.class, fileName));
            Assert.assertNotNull(input.getInput());
            final File targetFile = new File(dir, fileName);
            Files.createParentDirs(targetFile);
            Files.copy(input, targetFile);
        }

        cfg = Config.getConfig(new File(dir, "/test-config/basic").toURI(), "values");
        cfg2 = Config.getConfig(new File(dir, "/test-config/basic-legacy").toURI(), "values");
    }

    @After
    public void removeFiles() throws IOException
    {
        Assert.assertNotNull(dir);
        Assert.assertTrue(dir.isDirectory());
        FileUtils.deleteDirectory(dir);
    }
}


