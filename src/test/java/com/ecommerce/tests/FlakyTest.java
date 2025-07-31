package com.ecommerce.tests;

import org.testng.annotations.Test;

public class FlakyTest {

    @Test(invocationCount = 3)
    public void flakyTest() {
        int random = (int)(Math.random() * 10);
        System.out.println("Attempt: " + random);
        assert random > 5 : "Random was too low";
    }

}
