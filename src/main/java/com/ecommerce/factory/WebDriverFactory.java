package com.ecommerce.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;

public class WebDriverFactory {
    public static WebDriver getDriver(String browser) {
        WebDriver driver;

        switch (browser.toLowerCase()) {
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            case "chrome-headless":
                ChromeOptions headlessOptions = new ChromeOptions();
                headlessOptions.addArguments("--headless=new", "--disable-gpu");
                driver = new ChromeDriver(headlessOptions);
                break;
            default:
                driver = new ChromeDriver(); // default = Chrome
        }

        // Implicit wait (applies globally)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Maximize browser
        driver.manage().window().maximize();

        return driver;
    }
}

