package com.ecommerce.tests;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ParameterizedTest {

    @Test
    @Parameters({"browser"})
    public void testBrowser(String browser) {
        System.out.println("Browser passed from XML: " + browser);
    }
}
