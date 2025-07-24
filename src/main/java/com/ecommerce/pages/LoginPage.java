package com.ecommerce.pages;

import com.ecommerce.locators.LoginLocators;
import com.ecommerce.utils.IWaitStrategy;
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
            return toastText.contains("Login successful") || toastText.contains("Welcome");
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










/*
// src/main/java/com/ecommerce/pages/LoginPage.java
package com.ecommerce.pages;

import com.ecommerce.locators.LoginLocators;
import com.ecommerce.utils.IWaitStrategy;
import com.ecommerce.utils.SeleniumWaitStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class LoginPage extends AbstractBasePage {

    private IWaitStrategy waitStrategy;

    public LoginPage(WebDriver driver) {
        super(driver);
        this.waitStrategy = new SeleniumWaitStrategy();
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

    public void enterUsername(String username) {
        // Now this call is valid because the overload exists in IWaitStrategy and SeleniumWaitStrategy
        WebElement usernameField = waitStrategy.waitForElementToBeVisible(driver, LoginLocators.USERNAME_FIELD);
        usernameField.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement passwordField = waitStrategy.waitForElementToBeVisible(driver, LoginLocators.PASSWORD_FIELD);
        passwordField.sendKeys(password);
    }

    public void clickLoginButton() {
        WebElement loginButton = waitStrategy.waitForElementToBeClickable(driver, LoginLocators.LOGIN_BUTTON);
        loginButton.click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public void enterLevel(String level) {
        // Use your wait strategy to interact with the element
        waitStrategy.waitForElementToBeVisible(driver, LoginLocators.LEVEL_FIELD).sendKeys(level);
        // If it's a dropdown, you might do:
         new Select(waitStrategy.waitForElementToBeVisible(driver, LoginLocators.LEVEL_FIELD)).selectByVisibleText(level);
    }

}

 */


















// old
//package com.ecommerce.pages;
//
//import com.ecommerce.locators.LoginLocators;
//import org.openqa.selenium.*;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import java.time.Duration;
//
//
//public class LoginPage {
//    private final WebDriver driver;
//
//    public LoginPage(WebDriver driver) {
//        this.driver = driver;
//    }
//
//    public void doLogin(String user, String pass, String level) {
//        driver.findElement(LoginLocators.userName).sendKeys(user);
//        driver.findElement(LoginLocators.password).sendKeys(pass);
//        driver.findElement(LoginLocators.level).sendKeys(level);
//        driver.findElement(LoginLocators.loginBtn).click();
//    }
//
//    public boolean isLoginSuccessful() {
//        try {
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
//            WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(LoginLocators.loginToast));
//            return toast.getText().contains("Login successful");
//        } catch (TimeoutException e) {
//            return false;
//        }
//    }
//
//    public boolean isLoginSuccessful2() {
//        try {
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
//            WebElement dashboardText = wait.until(ExpectedConditions.visibilityOfElementLocated(LoginLocators.welcomeText));
//            return dashboardText.isDisplayed();
//        } catch (NoSuchElementException e) {
//            return false;
//        }
//    }
//
//    public boolean isLoginSuccessful3() {
//        try {
//            // return driver.findElement(LoginLocators.profileIcon).isDisplayed();
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
//            WebElement profileIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(LoginLocators.profileIcon));
//            return profileIcon.isDisplayed();
//        } catch (NoSuchElementException e) {
//            return false;
//        }
//    }
//
//    public boolean isLoginSuccessful4() {
//        return driver.getCurrentUrl().contains("loginLevels");
//    }
//
//
//
//}
