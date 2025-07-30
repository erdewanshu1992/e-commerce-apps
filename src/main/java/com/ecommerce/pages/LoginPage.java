package com.ecommerce.pages;

import com.ecommerce.locators.LoginLocators;
import com.ecommerce.interfaces.IWaitStrategy;
import com.ecommerce.utils.SeleniumWaitStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.Select;


public class LoginPage extends AbstractBasePage { // Or just implements ILoginPage if no AbstractBasePage
    private static final Logger logger = LogManager.getLogger(LoginPage.class);
    // You need an instance of IWaitStrategy in your Page Object
    // It can be instantiated directly here or passed via constructor/dependency injection
    private final IWaitStrategy waitStrategy;

    public LoginPage(WebDriver driver) {
        super(driver); // Call the constructor of AbstractBasePage
        // Instantiate the wait strategy here
        this.waitStrategy = new SeleniumWaitStrategy(); // Or get from a factory/DI
    }

    // Your existing methods like enterUsername, enterPassword, clickLoginButton...

    public boolean isLoginSuccessful() {
        try {
            WebElement successToast = waitStrategy.waitForElementToBeVisible(driver, LoginLocators.LOGIN_TOAST);
            String toastText = successToast.getText();
            logger.info("Login success toast/message displayed: {}", toastText);
            return toastText.contains("Login successful") || toastText.contains("Login");
        } catch (TimeoutException e) {
            logger.warn("Login success message/toast did not appear within timeout.", e);
            return false;
        } catch (Exception e) { // Catch other potential exceptions during text retrieval
            logger.error("An error occurred while checking login success status: {}", e.getMessage());
            return false;
        }
    }

    // You also need to make sure the methods that interact with elements use the waitStrategy
    public void enterUsername(String username) {
        waitStrategy.waitForElementToBeVisible(driver, LoginLocators.USERNAME_FIELD).sendKeys(username);
    }

    public void enterPassword(String password) {
        waitStrategy.waitForElementToBeVisible(driver, LoginLocators.PASSWORD_FIELD).sendKeys(password);
    }

    public void enterLevel(String level) {
        waitStrategy.waitForElementToBeVisible(driver, LoginLocators.LEVEL_FIELD).click();
        new Select(waitStrategy.waitForElementToBeVisible(driver, LoginLocators.LEVEL_FIELD)).selectByVisibleText(level);
    }

    public void clickLoginButton() {
        waitStrategy.waitForElementToBeClickable(driver, LoginLocators.LOGIN_BUTTON).click();
    }

    @Override
    public boolean isPageLoaded() {
        // Check if the login page is loaded by verifying the presence of the username field
        try {
            WebElement usernameField = waitStrategy.waitForElementToBeVisible(driver, LoginLocators.USERNAME_FIELD);
            return usernameField.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}