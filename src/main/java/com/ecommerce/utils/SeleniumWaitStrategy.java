package com.ecommerce.utils;

import com.ecommerce.interfaces.IWaitStrategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;


public class SeleniumWaitStrategy implements IWaitStrategy {

    private static final int DEFAULT_WAIT_SECONDS = 20;

    @Override
    public WebElement waitForElementToBeVisible(WebDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Implementing the new overloaded method
    @Override
    public WebElement waitForElementToBeVisible(WebDriver driver, By locator) {
        return waitForElementToBeVisible(driver, locator, DEFAULT_WAIT_SECONDS);
    }

    @Override
    public WebElement waitForElementToBeClickable(WebDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // Implementing the new overloaded method
    @Override
    public WebElement waitForElementToBeClickable(WebDriver driver, By locator) {
        return waitForElementToBeClickable(driver, locator, DEFAULT_WAIT_SECONDS);
    }

    @Override
    public WebElement waitForElementToBePresent(WebDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    // Implementing the new overloaded method
    @Override
    public WebElement waitForElementToBePresent(WebDriver driver, By locator) {
        return waitForElementToBePresent(driver, locator, DEFAULT_WAIT_SECONDS);
    }

    @Override
    public boolean waitForUrlContains(WebDriver driver, String urlFraction, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.urlContains(urlFraction));
    }

    // Implementing the new overloaded method
    @Override
    public boolean waitForUrlContains(WebDriver driver, String urlFraction) {
        return waitForUrlContains(driver, urlFraction, DEFAULT_WAIT_SECONDS);
    }
}