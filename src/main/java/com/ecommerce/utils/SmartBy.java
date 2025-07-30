package com.ecommerce.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import java.util.Arrays;
import java.util.List;

/**
 * A custom By object to store multiple locators for a single element,
 * ordered by preference. Used for self-healing capabilities.
 * The WebDriverUtils will use this to try locators in order.
 */
public class SmartBy extends By {

    private final List<By> locators;

    private SmartBy(By... locators) {
        if (locators == null || locators.length == 0) {
            throw new IllegalArgumentException("SmartBy must be initialized with at least one locator.");
        }
        this.locators = Arrays.asList(locators);
    }

    /**
     * Factory method to create a SmartBy instance with multiple fallback locators.
     * The locators will be tried in the order they are provided.
     * @param locators A varargs array of Selenium By locators.
     * @return A new SmartBy instance.
     */
    public static SmartBy by(By... locators) {
        return new SmartBy(locators);
    }

    /**
     * Returns the list of contained locators.
     * @return An unmodifiable list of By locators.
     */
    public List<By> getLocators() {
        return locators;
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        return List.of();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SmartBy: ");
        for (int i = 0; i < locators.size(); i++) {
            sb.append(locators.get(i).toString());
            if (i < locators.size() - 1) {
                sb.append(" OR ");
            }
        }
        return sb.toString();
    }
}