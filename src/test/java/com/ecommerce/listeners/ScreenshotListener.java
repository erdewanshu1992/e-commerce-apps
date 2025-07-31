package com.ecommerce.listeners;

import com.ecommerce.interfaces.WebDriverProvider;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class ScreenshotListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        Object testClass = result.getInstance();
        WebDriver driver = ((WebDriverProvider) testClass).getDriver();

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String filename = "screenshots/" + result.getName() + ".png";
        try {
            FileUtils.copyFile(src, new File(filename));
            System.out.println("Screenshot saved: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
