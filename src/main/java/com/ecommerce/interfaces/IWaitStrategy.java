package com.ecommerce.interfaces;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public interface IWaitStrategy {
    WebElement waitForElementToBeVisible(WebDriver driver, By locator, int timeoutInSeconds);
    WebElement waitForElementToBeClickable(WebDriver driver, By locator, int timeoutInSeconds);
    WebElement waitForElementToBePresent(WebDriver driver, By locator, int timeoutInSeconds);
    boolean waitForUrlContains(WebDriver driver, String urlFraction, int timeoutInSeconds);

    // NEW OVERLOADED METHODS with default timeout
    WebElement waitForElementToBeVisible(WebDriver driver, By locator);
    WebElement waitForElementToBeClickable(WebDriver driver, By locator);
    WebElement waitForElementToBePresent(WebDriver driver, By locator);
    boolean waitForUrlContains(WebDriver driver, String urlFraction);

    // All the WebDriverUtils logic and static field were removed from here.
    // They belong in WebDriverUtils.java
}