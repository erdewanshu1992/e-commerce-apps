// src/main/java/com/ecommerce/factory/IDriverFactory.java
package com.ecommerce.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Capabilities; // <--- Import Capabilities

public interface IDriverFactory {
    // Defines the contract for initializing a WebDriver for local execution
    WebDriver init_driver(String browserName);

    // Defines the contract for initializing a WebDriver for remote execution
    // 'capabilities' parameter explicitly states the expected type for browser options.
    WebDriver init_driver_remote(String browserName, String hubUrl, Capabilities capabilities);
}