// src/main/java/com/ecommerce/utils/WebDriverUtils.java

package com.ecommerce.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// REMOVED: import java.util.List; (if not used for other methods)

// ADD THIS IMPORT STATEMENT
import com.ecommerce.utils.SmartBy; // <--- THIS IS THE MISSING IMPORT

public class WebDriverUtils {

    private static final Logger logger = LogManager.getLogger(WebDriverUtils.class);
    // Instantiate your wait strategy once
    private static final IWaitStrategy waitStrategy = new SeleniumWaitStrategy();

    /**
     * Finds an element using SmartBy, trying multiple locators with a default wait strategy.
     * Logs which locator was successfully used or if all failed.
     *
     * @param driver The WebDriver instance.
     * @param smartBy The SmartBy object containing multiple locators.
     * @return The found WebElement.
     * @throws NoSuchElementException If the element is not found with any locator.
     */
    public static WebElement findElement(WebDriver driver, SmartBy smartBy) {
        for (By locator : smartBy.getLocators()) {
            try {
                // Use the wait strategy here for presence of the element
                WebElement element = waitStrategy.waitForElementToBePresent(driver, locator);
                logger.debug("Element found using locator: {}", locator.toString());
                return element;
            } catch (Exception e) { // Catch TimeoutException and NoSuchElementException
                logger.warn("Element not found with locator: {}. Trying next locator...", locator.toString());
            }
        }
        logger.error("Element not found with any of the provided locators: {}", smartBy.toString());
        throw new NoSuchElementException("Element not found with any locators: " + smartBy.toString());
    }

    /**
     * Overload for standard By locators, using the default wait strategy for presence.
     */
    public static WebElement findElement(WebDriver driver, By locator) {
        return waitStrategy.waitForElementToBePresent(driver, locator);
    }

    /**
     * Clicks on an element after waiting for it to be clickable.
     * @param driver The WebDriver instance.
     * @param locator The By locator of the element.
     */
    public static void clickElement(WebDriver driver, By locator) {
        waitStrategy.waitForElementToBeClickable(driver, locator).click();
        logger.info("Clicked element with locator: {}", locator.toString());
    }

    /**
     * Sends keys to an element after waiting for it to be visible and interactable.
     * @param driver The WebDriver instance.
     * @param locator The By locator of the element.
     * @param text The text to send.
     */
    public static void sendKeys(WebDriver driver, By locator, String text) {
        WebElement element = waitStrategy.waitForElementToBeVisible(driver, locator);
        element.clear(); // Clear existing text if any
        element.sendKeys(text);
        logger.info("Sent keys '{}' to element with locator: {}", text, locator.toString());
    }

    // Add more specialized methods as needed, e.g., getText, isDisplayed, etc.
}