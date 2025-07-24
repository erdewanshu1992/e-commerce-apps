// src/main/java/com/ecommerce/utils/WaitUtils.java

package com.ecommerce.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    private static final int DEFAULT_WAIT_SECONDS = 10; // Default wait time

    /**
     * Waits for an element to be visible on the page.
     * @param driver The WebDriver instance.
     * @param locator The By locator of the element.
     * @param timeoutInSeconds The maximum time to wait in seconds.
     * @return The WebElement once it is visible.
     */
    public static WebElement waitForElementToBeVisible(WebDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForElementToBeVisible(WebDriver driver, By locator) {
        return waitForElementToBeVisible(driver, locator, DEFAULT_WAIT_SECONDS);
    }

    /**
     * Waits for an element to be clickable.
     * @param driver The WebDriver instance.
     * @param locator The By locator of the element.
     * @param timeoutInSeconds The maximum time to wait in seconds.
     * @return The WebElement once it is clickable.
     */
    public static WebElement waitForElementToBeClickable(WebDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForElementToBeClickable(WebDriver driver, By locator) {
        return waitForElementToBeClickable(driver, locator, DEFAULT_WAIT_SECONDS);
    }

    /**
     * Waits for an element to be present in the DOM.
     * @param driver The WebDriver instance.
     * @param locator The By locator of the element.
     * @param timeoutInSeconds The maximum time to wait in seconds.
     * @return The WebElement once it is present.
     */
    public static WebElement waitForElementToBePresent(WebDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static WebElement waitForElementToBePresent(WebDriver driver, By locator) {
        return waitForElementToBePresent(driver, locator, DEFAULT_WAIT_SECONDS);
    }

    /**
     * Waits until the URL contains a specific text.
     * @param driver The WebDriver instance.
     * @param urlFraction The text expected to be in the URL.
     * @param timeoutInSeconds The maximum time to wait in seconds.
     * @return True if the URL contains the text, false otherwise.
     */
    public static boolean waitForUrlContains(WebDriver driver, String urlFraction, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.urlContains(urlFraction));
    }
}