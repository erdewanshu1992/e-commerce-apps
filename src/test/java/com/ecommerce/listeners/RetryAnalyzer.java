package com.ecommerce.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implements TestNG's IRetryAnalyzer to re-run failed tests a specified number of times.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);
    private int retryCount = 0;
    // Max retries allowed, can be configured via a property if needed
    private static final int MAX_RETRY_COUNT = 1; // Generally 1 or 2 is sufficient to handle transient issues

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            logger.warn("Retrying test: '{}' - Attempt {} of {}",
                    result.getName(), (retryCount + 1), MAX_RETRY_COUNT);
            retryCount++;
            return true; // Retry the test
        }
        return false; // Do not retry
    }
}