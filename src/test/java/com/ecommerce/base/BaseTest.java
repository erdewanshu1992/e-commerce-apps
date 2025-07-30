package com.ecommerce.base;

import com.ecommerce.database.DBUtil;
import com.ecommerce.factory.DriverFactory;
import com.ecommerce.interfaces.IDriverFactory;
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
    private static final IDriverFactory driverFactory = new DriverFactory(); // Use interface
    protected WebDriver driver;

    // Using TestNG Parameters to get the environment from testng.xml
    @BeforeSuite
    @Parameters({"environment"})
    public void setupSuite(@Optional("qa") String environment) {
        logger.info("Setting up test suite for environment: {}", environment);
        // This is correct: Initialize properties for the given environment
        ConfigReader.init_prop(environment); // This loads the properties into ThreadLocal

        // Now, when accessing db.enabled, it should correctly reflect the file.
        String dbEnabled = ConfigReader.getProperty("db.enabled", "/api/users");
        if ("true".equalsIgnoreCase(dbEnabled)) {
            logger.info("Database interaction is enabled (db.enabled=true). Attempting to establish connection.");
            DBUtil.establishConnection(); // Call establishConnection directly here
            // Example: Clean up database before suite starts
            DBUtil.executeUpdate("DELETE FROM credentials WHERE username LIKE '%testuser%';"); // Adjust based on common test usernames

            // OR, if you want to clean up specific test data before each run
            // For instance, if you insert specific test users for each run, and want to delete them later:
            // You could also just use DELETE FROM credentials; with caution, or DELETE FROM credentials WHERE username = 'testuser1';
            // It's common to delete by a pattern if your test users have a consistent naming convention.
            // For example, if your test users are like 'testuser_1', 'testuser_2', 'testuser_db', etc.
            // For simple cleanup, you might just want to delete known test accounts
            DBUtil.executeUpdate("DELETE FROM credentials WHERE username = 'testuser1' OR username = 'testuser2';");
            // Or, if you just want to clear all data in the credentials table (use with extreme caution on production-like databases!)
            // DBUtil.executeUpdate("TRUNCATE TABLE credentials;"); // TRUNCATE is faster for full table clear
            // DBUtil.executeUpdate("DELETE FROM credentials;"); // DELETE for full table clear (slower, but keeps auto-increment)
            logger.info("Database initialized and cleaned up.");
        } else {
            logger.info("Database interaction skipped (db.enabled=false).");
        }
    }

    @AfterSuite
    public void tearDownSuite() {
        logger.info("Tearing down test suite.");
        String dbEnabled = ConfigReader.getProperty("db.enabled", "/api/users");
        if ("true".equalsIgnoreCase(dbEnabled)) {
            DBUtil.closeConnection();
            logger.info("Database connection closed.");
        }
        ConfigReader.clearProperties();
    }

    @BeforeMethod
    // @BeforeClass
    public void setup() {
        logger.info("Starting test method setup.");

        String browserName = ConfigReader.getProperty("browser", "/api/users");
        String executionType = ConfigReader.getProperty("execution.type", "/api/users");
        String appUrl = ConfigReader.getProperty("app.url", "/api/users");

        if ("local".equalsIgnoreCase(executionType)) {
            driver = driverFactory.init_driver(browserName);
        } else if ("remote".equalsIgnoreCase(executionType)) {
            String hubUrl = ConfigReader.getProperty("selenium.hub.url", "/api/users");

            // --- IMPORTANT CHANGE HERE ---
            // You need to create a Capabilities object (e.g., ChromeOptions, FirefoxOptions)
            // or pass null and let DriverFactory handle defaults.
            Capabilities capabilities = getCapabilities(browserName);
            // ... add other browser types as needed

            driver = driverFactory.init_driver_remote(browserName, hubUrl, capabilities);
        } else {
            logger.error("Invalid execution type: {}. Must be 'local' or 'remote'.", executionType);
            throw new IllegalArgumentException("Invalid execution type.");
        }

        driver.get(appUrl);
        logger.info("Navigated to URL: {}", appUrl);
    }

    private static Capabilities getCapabilities(String browserName) {
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
        return capabilities;
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