package com.ecommerce.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    // Using ThreadLocal to store properties. This ensures thread-safety if
    // you were to run multiple test suites in parallel, each needing a different config.
    // For a single testng.xml, a simple static Properties field is also fine.
    private static ThreadLocal<Properties> propThreadLocal = new ThreadLocal<>();

    /**
     * Initializes the Properties object by loading configuration from the specified environment file.
     * This method should be called once at the beginning of the test suite (e.g., in @BeforeSuite).
     *
     * @param env The environment name (e.g., "dev", "qa", "prod", "local").
     * @return The loaded Properties object.
     * @throws RuntimeException if the configuration file cannot be found or loaded.
     */
    public static Properties init_prop(String env) {
        Properties prop = new Properties();
        String configFilePath = "src/main/resources/config-" + env.toLowerCase() + ".properties";
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(configFilePath);
            prop.load(fis);
            propThreadLocal.set(prop); // Store the loaded properties in ThreadLocal
            logger.info("Configuration loaded successfully from: {}", configFilePath);
            return prop;
        } catch (IOException e) {
            logger.fatal("FATAL ERROR: Could not load configuration file: {}", configFilePath, e);
            throw new RuntimeException("Failed to load configuration file: " + configFilePath + ". Error: " + e.getMessage(), e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error("Error closing FileInputStream for {}", configFilePath, e);
                }
            }
        }
    }

    /**
     * Retrieves the Properties object for the current thread.
     *
     * @return The Properties object.
     * @throws IllegalStateException if init_prop() has not been called for the current thread.
     */
    public static Properties getProperties() {
        Properties prop = propThreadLocal.get();
        if (prop == null) {
            logger.error("Properties object not initialized. Call ConfigReader.init_prop() first.");
            throw new IllegalStateException("Properties object not initialized. Call ConfigReader.init_prop() first.");
        }
        return prop;
    }

    /**
     * Retrieves a property value by its key from the loaded configuration.
     *
     * @param key The key of the property.
     * @return The property value, or null if not found.
     */
    public static String getProperty(String key) {
        return getProperties().getProperty(key);
    }

    /**
     * Clears the ThreadLocal property for the current thread.
     * Useful for cleaning up resources after a test suite.
     */
    public static void clearProperties() {
        propThreadLocal.remove();
        logger.debug("Properties cleared for current thread.");
    }
}











//package com.ecommerce.utils;
//
//import java.io.FileInputStream;
//import java.util.Properties;
//
//public class ConfigReader {
//    private final Properties prop;
//
//    public ConfigReader(String env) {
//        try {
//            FileInputStream fis = new FileInputStream("src/main/resources/config-" + env + ".properties");
//            prop = new Properties();
//            prop.load(fis);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to load config for env: " + env);
//        }
//    }
//
//    public String getProperty(String key) {
//        return prop.getProperty(key);
//    }
//}
