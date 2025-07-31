package com.ecommerce.tests;

import com.ecommerce.interfaces.WebDriverProvider;
import com.ecommerce.listeners.ScreenshotListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

@Listeners(ScreenshotListener.class)
public class ListenerTest implements WebDriverProvider {
    WebDriver driver;

    @Override
    public WebDriver getDriver() {
        return driver;
    }

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.get("http://automation-playground.local/automation-pages/loginLevelss.html");
    }

    @Test
    public void failingTest() {
        assert false : "Failing deliberately";
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }
}
