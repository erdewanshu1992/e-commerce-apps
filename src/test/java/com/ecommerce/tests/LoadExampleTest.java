package com.ecommerce.tests;

import org.testng.annotations.Test;

public class LoadExampleTest {

    @Test(
            invocationCount = 5,       // Run 5 times
            threadPoolSize = 2,        // Use 2 parallel threads
            timeOut = 3000             // Total timeout: 3 seconds (across all 5 runs)
    )
    public void simulateLoad() throws InterruptedException {
        long threadId = Thread.currentThread().getId();
        System.out.println("Running on Thread: " + threadId + " | Time: " + System.currentTimeMillis());

        // Simulate delay
        Thread.sleep(1000); // 1 second per invocation
    }

    @Test(invocationCount = 5)
    public void sampleTest() {
        System.out.println("Executed by Thread: " + Thread.currentThread().getId());
    }

}
