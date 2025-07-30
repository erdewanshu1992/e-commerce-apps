package com.ecommerce.listeners;

import com.ecommerce.utils.ExtentReportManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TestNG Listener to capture and log the execution duration of each test method.
 * Provides basic performance insights.
 */
public class PerformanceListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(PerformanceListener.class);
    // Use ConcurrentHashMap for thread safety in parallel execution
    private final ConcurrentHashMap<String, Long> testStartTime = new ConcurrentHashMap<>();

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        testStartTime.put(testName, System.currentTimeMillis());
        logger.debug("Performance tracking started for test: {}", testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        recordTestDuration(result, "PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        recordTestDuration(result, "FAILED");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        recordTestDuration(result, "SKIPPED");
    }

    private void recordTestDuration(ITestResult result, String status) {
        String testName = result.getMethod().getMethodName();
        Long startTime = testStartTime.remove(testName); // Remove to prevent memory leaks and track once
        if (startTime != null) {
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Performance Metric: Test '{}' finished with status '{}' in {} ms.", testName, status, duration);
            // In a more advanced scenario, you could push this to a time-series database (e.g., InfluxDB)
            // or integrate with ExtentReports to add a dedicated performance section.
        } else {
            logger.warn("Test start time not found for: {}. Duration could not be recorded.", testName);
        }
    }

    @Override
    public void onStart(ITestContext context) {
        // You can add suite-level setup for ExtentReports here if needed,
        // although getReportObject() handles lazy initialization.
        // It's a good place for logging suite start in ExtentReports if desired.
        ExtentReportManager.getReportObject(); // Ensures report object is initialized early
        logger.info("TestNGListener: Test suite '{}' started.", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("PerformanceListener finished for Test Suite: {}", context.getSuite().getName());
        // You could aggregate overall test suite performance here
    }
}