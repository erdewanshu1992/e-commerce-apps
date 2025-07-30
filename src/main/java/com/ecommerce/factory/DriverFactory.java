package com.ecommerce.factory;

import com.ecommerce.interfaces.IDriverFactory;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Factory class to initialize WebDriver instances for different browsers.
 * Supports both local and remote WebDriver initialization.
 */
public class DriverFactory implements IDriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class);
    public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    @Override
    public WebDriver init_driver(String browserName) {
        logger.info("Initializing local driver for browser: {}", browserName);

        switch (browserName.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                // Add common chrome options here
                tlDriver.set(new ChromeDriver(chromeOptions));
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                // Add common firefox options here
                tlDriver.set(new FirefoxDriver(firefoxOptions));
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                // Add common-edge options here
                tlDriver.set(new EdgeDriver(edgeOptions));
                break;
            default:
                logger.error("Unsupported browser: {}", browserName);
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }

        setupCommonDriverProperties();
        return getDriver();
    }

    // Corrected method signature: 'Object o' is now 'Capabilities'
    @Override
    public WebDriver init_driver_remote(String browserName, String hubUrl, Capabilities capabilities) {
        logger.info("Initializing remote driver for browser: {} on hub: {}", browserName, hubUrl);
        try {
            // If capabilities are null, set basic ones based on browserName
            // In a real advanced framework, you'd likely load these capabilities
            // from a JSON file or a dedicated capability factory/manager.
            if (capabilities == null) {
                if (browserName.equalsIgnoreCase("chrome")) {
                    capabilities = new ChromeOptions(); // Default Chrome capabilities
                } else if (browserName.equalsIgnoreCase("firefox")) {
                    capabilities = new FirefoxOptions(); // Default Firefox capabilities
                } else if (browserName.equalsIgnoreCase("edge")) {
                    capabilities = new EdgeOptions(); // Default Edge capabilities
                } else {
                    logger.warn("No specific capabilities provided for remote browser: {}. Attempting with default options.", browserName);
                    // As a fallback, you might even return null or throw if no default exists.
                    // For now, we'll let RemoteWebDriver potentially throw if capabilities are truly needed and null.
                }
            }
            tlDriver.set(new RemoteWebDriver(new URL(hubUrl), capabilities));
        } catch (MalformedURLException e) {
            logger.error("Invalid Selenium Hub URL: {}", hubUrl, e);
            throw new RuntimeException("Invalid Selenium Hub URL", e);
        }
        setupCommonDriverProperties();
        return getDriver();
    }

    private void setupCommonDriverProperties() {
        getDriver().manage().deleteAllCookies();
        getDriver().manage().window().maximize();
        logger.info("Driver common properties set (cookies deleted, window maximized).");
    }

    public static synchronized WebDriver getDriver() {
        return tlDriver.get();
    }
}
