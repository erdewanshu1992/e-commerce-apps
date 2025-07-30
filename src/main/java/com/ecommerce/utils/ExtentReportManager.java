package com.ecommerce.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager {

    private static ExtentReports extent;
    private static final String reportFilePath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";

    public synchronized static ExtentReports getReportObject() {
        if (extent == null) {
            ExtentSparkReporter reporter = new ExtentSparkReporter(reportFilePath);
            reporter.config().setReportName("E-commerce Test Automation Results");
            reporter.config().setDocumentTitle("Test Results");
            reporter.config().setTheme(Theme.STANDARD); // or Theme.DARK

            extent = new ExtentReports();
            extent.attachReporter(reporter);
            extent.setSystemInfo("Tester", "Your Name"); // Customize
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        }
        return extent;
    }

    public synchronized static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}