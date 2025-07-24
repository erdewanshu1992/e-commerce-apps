package com.ecommerce.locators;

import org.openqa.selenium.By;

public class LoginLocators {
    // Locator constants for the login page
    public static final By USERNAME_FIELD = By.id("username");
    public static final By PASSWORD_FIELD = By.id("password");
    public static final By LEVEL_FIELD = By.id("level");
    public static final By LOGIN_BUTTON = By.id("loginBtn");
    public static final By LOGIN_TOAST = By.id("message");
    public static final By WELCOME_TEXT = By.id("welcomeBackMsg");
    public static final By PROFILE_ICON = By.id("loggedInUsername");
}
