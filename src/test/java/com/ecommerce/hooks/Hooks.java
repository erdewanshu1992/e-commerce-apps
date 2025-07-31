package com.ecommerce.hooks;


import com.ecommerce.factory.DriverFactory;
import com.ecommerce.factory.WebDriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import java.time.Duration;

public class Hooks {

    public static WebDriver driver;


    @Before
    public void setUp() {
        driver = WebDriverFactory.getDriver("chrome");
        DriverFactory.setDriver(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3000));
    }


    /*
    @Before
    public void setUp() {
        driver = WebDriverManager.chromedriver().create();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3000));
    }
     */

    @After
    public void tearDown() {
        driver.quit();
    }
}
