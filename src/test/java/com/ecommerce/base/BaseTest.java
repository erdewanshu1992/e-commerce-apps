// src/test/java/com/ecommerce/base/BaseTest.java (modified)
package com.ecommerce.base;

import com.ecommerce.database.DBUtil;
import com.ecommerce.factory.DriverFactory;
import com.ecommerce.factory.IDriverFactory;
import com.ecommerce.utils.ConfigReader;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BaseTest {

    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    private static IDriverFactory driverFactory = new DriverFactory(); // Use interface
    protected WebDriver driver;

    // Using TestNG Parameters to get the environment from testng.xml
    @BeforeSuite
    @Parameters({"environment"}) // This parameter will be passed from testng.xml
    public void setupSuite(@Optional("qa") String environment) { // "qa" as default if not specified
        logger.info("Setting up test suite for environment: {}", environment);
        // Initialize properties for the given environment
        // The ConfigReader now directly manages the Properties object internally via ThreadLocal
        ConfigReader.init_prop(environment);

        // Access properties using ConfigReader.getProperty() or ConfigReader.getProperties()
        String dbEnabled = ConfigReader.getProperty("db.enabled"); // Example property for DB
        if ("true".equalsIgnoreCase(dbEnabled)) {
            DBUtil.establishConnection();
            // Example: Clean up database before suite starts
            DBUtil.executeUpdate("DELETE FROM users WHERE email LIKE '%testuser%';");
            logger.info("Database initialized and cleaned up.");
        } else {
            logger.info("Database interaction skipped (db.enabled=false).");
        }
    }

    @AfterSuite
    public void tearDownSuite() {
        logger.info("Tearing down test suite.");
        String dbEnabled = ConfigReader.getProperty("db.enabled"); // Access property using ConfigReader
        if ("true".equalsIgnoreCase(dbEnabled)) {
            DBUtil.closeConnection();
            logger.info("Database connection closed.");
        }
        ConfigReader.clearProperties(); // Clean up ThreadLocal properties
    }

    @BeforeMethod
    // @BeforeClass
    public void setup() {
        logger.info("Starting test method setup.");

        String browserName = ConfigReader.getProperty("browser");
        String executionType = ConfigReader.getProperty("execution.type");
        String appUrl = ConfigReader.getProperty("app.url");

        if ("local".equalsIgnoreCase(executionType)) {
            driver = driverFactory.init_driver(browserName);
        } else if ("remote".equalsIgnoreCase(executionType)) {
            String hubUrl = ConfigReader.getProperty("selenium.hub.url");

            // --- IMPORTANT CHANGE HERE ---
            // You need to create a Capabilities object (e.g., ChromeOptions, FirefoxOptions)
            // or pass null and let DriverFactory handle defaults.
            Capabilities capabilities = null; // Initialize to null

            // Example of creating capabilities based on browser name
            if ("chrome".equalsIgnoreCase(browserName)) {
                capabilities = new ChromeOptions();
                // Add specific Chrome options for remote execution if needed
                // ((ChromeOptions) capabilities).addArguments("--no-sandbox");
            } else if ("firefox".equalsIgnoreCase(browserName)) {
                capabilities = new FirefoxOptions();
                // Add specific Firefox options
            }
            // ... add other browser types as needed

            driver = driverFactory.init_driver_remote(browserName, hubUrl, capabilities);
        } else {
            logger.error("Invalid execution type: {}. Must be 'local' or 'remote'.", executionType);
            throw new IllegalArgumentException("Invalid execution type.");
        }

        driver.get(appUrl);
        logger.info("Navigated to URL: {}", appUrl);
    }

    @AfterMethod
    // @AfterClass
    public void tearDown() {
        if (driver != null) {
            logger.info("Closing browser.");
            driver.quit();
        }
        logger.info("Test method teardown complete.");
    }
}










//package com.ecommerce.base;
//
//import com.ecommerce.factory.DriverFactory;
//import com.ecommerce.utils.ConfigReader;
//import org.openqa.selenium.WebDriver;
//import org.testng.annotations.*;
//
//
//public class BaseTest {
//    protected WebDriver driver;
//    protected ConfigReader config;
//
//
//    @Parameters({"browser", "env"})
//    @BeforeClass
//    public void setUp(@Optional("chrome") String browser, @Optional("qa") String env) {
//        String finalBrowser = System.getProperty("browser", browser); // 🔁 CLI > testng.xml > default
//        String finalEnv = System.getProperty("env", env); // 🔁 CLI > testng.xml > default
//
//        System.out.println("🌐 Browser: " + finalBrowser);
//        System.out.println("🏁 Running tests in environment: " + finalEnv);
//
//        config = new ConfigReader(finalEnv);
//        driver = DriverFactory.initDriver(finalBrowser);
//        driver.get(config.getProperty("url"));
//        System.out.println("=========================================");
//        System.out.println("🔗 Base URL: " + config.getProperty("url"));
//        System.out.println("🔄 Test setup completed. Ready to run tests.");
//        System.out.println("=========================================");
//    }
//
//
//    @AfterClass
//    public void tearDown() {
//        driver.quit();
//        System.out.println("🛑 Test execution completed. WebDriver closed.");
//    }
//}



// mvn clean test -Denv=dev
// mvn test -Dbrowser=firefox -Denv=dev
// mvn test -Dbrowser=chrome -Denv=qa
// mvn test -Dbrowser=chrome -Denv=prod
// mvn test -Dbrowser=chrome -Denv=staging
// mvn test -Dbrowser=chrome -Denv=uat
// mvn test -Dbrowser=chrome -Denv=local
// mvn test -Dbrowser=chrome -Denv=local -Dtest=LoginTest
// tree -I 'target|.git|node_modules'
// tree -I 'target|.git|node_modules' > project-structure.txt