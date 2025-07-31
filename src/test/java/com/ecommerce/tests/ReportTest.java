package com.ecommerce.tests;

import com.ecommerce.reporters.CustomReporter;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(CustomReporter.class)
public class ReportTest {

    @Test
    public void testPass() {
        assert true;
    }

    @Test
    public void testFail() {
        assert false;
    }

    @Test(enabled = false)
    public void testSkip() {}
}

