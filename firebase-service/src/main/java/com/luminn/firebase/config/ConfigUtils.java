package com.luminn.firebase.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtils {
    private static final Logger log = LoggerFactory.getLogger(ConfigUtils.class);

    public static Properties getProperties(String propFilename) {
        Properties properties = new Properties();
        try {
            File file = ResourceUtils.getFile(propFilename); //"classpath:kvu.properties");
            InputStream in = new FileInputStream(file);
            properties.load(in);
        } catch (IOException e) {
            log.error("ConfigUtils.getProperties(): propFilename=" + propFilename + " IOException message: " + e.getMessage());
            new RuntimeException(e);
        }
        return properties;
    }
}
