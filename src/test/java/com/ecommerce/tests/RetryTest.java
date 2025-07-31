package com.ecommerce.tests;

import com.ecommerce.listeners.RetryAnalyzer;
import org.testng.annotations.Test;

public class RetryTest {

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void sampleTest() {
        assert false : "Failing test to check retry logic";
    }
}
