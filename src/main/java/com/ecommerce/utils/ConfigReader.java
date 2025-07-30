package com.ecommerce.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public final class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    // THIS LINE IS CRUCIAL AND MUST BE PRESENT
    private static final ThreadLocal<Properties> threadSafeProperties = new ThreadLocal<>();

    private ConfigReader() {
        // Private constructor to prevent instantiation
    }

    public static void init_prop(String environment) {
        Properties prop = new Properties();
        FileInputStream ip = null;
        try {
            String configFilePath = "src/main/resources/config-" + environment + ".properties";
            logger.info("Attempting to load configuration from: {}", configFilePath);

            File configFile = new File(configFilePath);
            if (!configFile.exists()) {
                logger.error("Configuration file NOT FOUND at: {}", configFile.getAbsolutePath());
                throw new IOException("Config file not found: " + configFilePath);
            }

            ip = new FileInputStream(configFile);
            prop.load(ip);
            // THIS LINE USES 'threadSafeProperties'
            threadSafeProperties.set(prop);
            logger.info("Configuration loaded successfully from: {}", configFilePath);
            logger.info("Value of 'db.enabled' directly from loaded properties: {}", prop.getProperty("db.enabled"));

        } catch (IOException e) {
            logger.error("Error loading configuration properties from {}: {}", e.getMessage(), e);
            throw new RuntimeException("Error loading configuration properties file.", e);
        } finally {
            if (ip != null) {
                try {
                    ip.close();
                } catch (IOException e) {
                    logger.error("Error closing FileInputStream: {}", e.getMessage());
                }
            }
        }
    }

    public static String getProperty(String key, String path) {
        // THIS LINE USES 'threadSafeProperties'
        Properties prop = threadSafeProperties.get();
        if (prop == null) {
            logger.error("Properties not initialized. Call ConfigReader.init_prop() firsts.");
            throw new IllegalStateException("ConfigReader properties not initialized.");
        }
        String value = prop.getProperty(key);
        if (value == null) {
            logger.warn("Property '{}' not found in the configuration.", key);
        }
        return value;
    }

    public static Properties getProperties() {
        // THIS LINE USES 'threadSafeProperties'
        Properties prop = threadSafeProperties.get();
        if (prop == null) {
            logger.error("Properties not initialized. Call ConfigReader.init_prop() first.");
            throw new IllegalStateException("ConfigReader properties not initialized.");
        }
        return prop;
    }

    public static void clearProperties() {
        // THIS LINE USES 'threadSafeProperties'
        threadSafeProperties.remove();
        logger.info("Cleared ThreadLocal properties.");
    }
}