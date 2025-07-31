package com.ecommerce.tests;

import org.testng.annotations.Test;

public class ParallelTest {

    @Test(invocationCount = 4, threadPoolSize = 2)
    public void parallelTestVerify() {
        System.out.println("Running: " + Thread.currentThread().getId());
    }

}
