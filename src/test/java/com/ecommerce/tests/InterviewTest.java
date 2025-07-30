package com.ecommerce.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class InterviewTest {
    WebDriver webDriver;

    @BeforeMethod
    public void setUp() {
        // Initialize WebDriver using WebDriverManager
        // WebDriverManager.chromedriver().setup();
        // webDriver = new ChromeDriver();
        // Directly create the driver, no need to call setup separately
        webDriver = WebDriverManager.chromedriver().create();
        webDriver.manage().window().maximize();
        // Load the login page URL
        webDriver.get("https://codility-frontend-prod.s3.amazonaws.com/media/task_static/qa_login_page/9a83bda125cd7398f9f482a3d6d45ea4/static/attachments/reference_page.html");
    }

    // ✅ 1. Check if email, password, and login button are visible on the login page
    @Test
    public void shouldDisplayLoginFormFields() {
        assertTrue(webDriver.findElement(By.id("email-input")).isDisplayed());
        assertTrue(webDriver.findElement(By.id("password-input")).isDisplayed());
        assertTrue(webDriver.findElement(By.id("login-button")).isDisplayed());
    }

    // ✅ 2. Valid credentials should login successfully and show welcome message
    @Test
    public void shouldLoginSuccessfullyWithValidCredentials() {
        webDriver.findElement(By.id("email-input")).sendKeys("login@codility.com");
        webDriver.findElement(By.id("password-input")).sendKeys("password");
        webDriver.findElement(By.id("login-button")).click();

        WebElement successMsg = webDriver.findElement(By.cssSelector(".message.success"));
        assertTrue(successMsg.isDisplayed());
        // assertEquals("Welcome to Codility", successMsg.getText());
        assertEquals(successMsg.getText(), "Welcome to Codility");
    }

    // ✅ 3. Invalid credentials should show error message
    @Test
    public void shouldShowErrorMessageForInvalidCredentials() {
        webDriver.findElement(By.id("email-input")).sendKeys("unknown@codility.com");
        webDriver.findElement(By.id("password-input")).sendKeys("password");
        webDriver.findElement(By.id("login-button")).click();

        WebElement errorMsg = webDriver.findElement(By.cssSelector(".message.error"));
        assertTrue(errorMsg.isDisplayed());
        // assertEquals("You shall not pass! Arr!", errorMsg.getText());
        assertEquals(errorMsg.getText(), "You shall not pass! Arr!");
    }

    // ✅ 4. Invalid email format should trigger validation error
    @Test
    public void shouldShowValidationErrorForInvalidEmailFormat() {
        webDriver.findElement(By.id("email-input")).sendKeys("invalid-email");
        webDriver.findElement(By.id("password-input")).sendKeys("password");
        webDriver.findElement(By.id("login-button")).click();

        WebElement validationMsg = webDriver.findElement(By.cssSelector(".validation.error"));
        assertTrue(validationMsg.isDisplayed());
        // assertEquals("Enter a valid email", validationMsg.getText());
        assertEquals(validationMsg.getText(), "Enter a valid email");
    }

    // ✅ 5. Empty email & password fields should show validation errors
    @Test
    public void shouldShowErrorForEmptyCredentials() {
        webDriver.findElement(By.id("login-button")).click();

        WebElement emailError = webDriver.findElement(By.xpath("//div[contains(@class, 'validation error') and text()='Email is required']"));
        WebElement passwordError = webDriver.findElement(By.xpath("//div[contains(@class, 'validation error') and text()='Password is required']"));

        assertTrue(emailError.isDisplayed());
        assertTrue(passwordError.isDisplayed());
    }

    // ✅ 6. TAB and ENTER keys should work as expected
    @Test
    public void shouldSupportTabAndEnterKeys() {
        WebElement emailField = webDriver.findElement(By.id("email-input"));
        WebElement passwordField = webDriver.findElement(By.id("password-input"));

        emailField.sendKeys("login@codility.com");
        emailField.sendKeys(Keys.TAB); // Move to password

        WebElement focusedElement = webDriver.switchTo().activeElement();
        assertEquals(passwordField, focusedElement); // Check focus is on password

        passwordField.sendKeys("password");
        // passwordField.sendKeys(Keys.ENTER); // Submit form

        webDriver.findElement(By.id("login-button")).click();

        WebElement successMsg = webDriver.findElement(By.cssSelector(".message.success"));
        assertTrue(successMsg.isDisplayed()); // Junit assertion
        // assertEquals("Welcome to Codility", successMsg.getText());
        assertEquals(successMsg.getText(), "Welcome to Codility"); // Junit assertion

        Assert.assertTrue(successMsg.isDisplayed()); // TestNG assertion
        Assert.assertEquals(successMsg.getText(), "Welcome to Codility"); // TestNG assertion

    }
}
