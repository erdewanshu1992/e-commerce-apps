package com.ecommerce.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status; // This is the Status enum
import com.ecommerce.base.BaseTest;
import com.ecommerce.utils.ExtentReportManager;
import com.ecommerce.utils.ScreenshotUtil;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

public class TestNGListener extends BaseTest implements ITestListener {

    // ExtentReports instance
    ExtentReports extent = ExtentReportManager.getReportObject();
    // ThreadLocal to handle parallel test execution for ExtentTest
    ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        // Create a new test entry in the report for each test method
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test); // Store the test instance in ThreadLocal
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // Log the main test failure with the exception
        extentTest.get().fail(result.getThrowable());

        WebDriver driver = null;
        try {
            // Get the WebDriver instance from the BaseTest class during runtime
            // This line assumes 'driver' field is public static in BaseTest or accessible.
            // A safer approach might be to pass the driver to the listener or have a ThreadLocal for driver in DriverFactory.
            // As per our setup, driver is in DriverFactory.tlDriver
            // Let's refine how you get the driver here for robustness:
            driver = com.ecommerce.factory.DriverFactory.getDriver(); // Directly get from DriverFactory's ThreadLocal
        } catch (Exception e) {
            // Log that driver could not be obtained for screenshot
            // Use Status.WARNING or Status.INFO here as it's a reporting issue, not main test failure
            extentTest.get().log(Status.WARNING, "Could not obtain WebDriver instance for screenshot: " + e.getMessage());
            e.printStackTrace(); // Still print stack trace for debugging purposes
        }

        if (driver != null) {
            try {
                String screenshotPath = ScreenshotUtil.captureScreenshot(driver, result.getMethod().getMethodName());
                // Log screenshot addition with INFO or PASS if it's supplemental for a failed test
                extentTest.get().addScreenCaptureFromPath(screenshotPath, "Failed Test Screenshot");
                extentTest.get().log(Status.INFO, "Screenshot captured for failed test: " + result.getMethod().getMethodName());
            } catch (IOException e) {
                e.printStackTrace(); // Log screenshot capture error
                // Use Status.WARNING or Status.INFO, as the test already failed. This is a reporting issue.
                extentTest.get().log(Status.WARNING, "Could not capture screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP, "Test Skipped");
        // You might also log the reason for skipping if result.getThrowable() provides it
        if (result.getThrowable() != null) {
            extentTest.get().log(Status.SKIP, result.getThrowable());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        // Flush the report to write all test information to the HTML file
        ExtentReportManager.flushReport();
    }

    // Other ITestListener methods can be overridden if needed (onStart, onTestFailedButWithinSuccessPercentage, etc.)
}