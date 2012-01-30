package com.likeness.config;

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
import com.likeness.config.Config;

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


