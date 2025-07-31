package com.ecommerce.interfaces;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Capabilities;

public interface IDriverFactory {

    WebDriver init_driver(String browserName);
    WebDriver init_driver_remote(String browserName, String hubUrl, Capabilities capabilities);
}