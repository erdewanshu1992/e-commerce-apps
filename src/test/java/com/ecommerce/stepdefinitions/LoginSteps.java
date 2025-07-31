package com.ecommerce.stepdefinitions;

import com.ecommerce.factory.DriverFactory;
import com.ecommerce.pages.LoginPage;
import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.time.Duration;


public class LoginSteps {

    WebDriver driver;
    LoginPage loginPage;
    String username;  // store value here
    String level;

    @Given("user is on login page")
    public void user_is_on_login_page() {
         driver = DriverFactory.getDriver();
        // driver = WebDriverManager.chromedriver().create();
        driver.get("http://automation-playground.local/automation-pages/loginLevelss.html");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3000));
        loginPage = new LoginPage(driver);
    }

    @When("user enters username {string}")
    public void user_enters_username(String username) {
        this.username = username; // store for later assertion
        loginPage.enterUsername(username);
    }

    @When("user enters password {string}")
    public void user_enters_password(String password) {
        loginPage.enterPassword(password);
    }

    @When("user selects level {string}")
    public void user_selects_level(String level) {
        this.level = level; // store for later assertion
        loginPage.enterLevel(level);
    }

    @When("user clicks the login button")
    public void user_clicks_the_login_button() {
        loginPage.clickLoginButton();
    }

    @Then("user should see the dashboard")
    public void user_should_see_the_dashboard() {
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful for: " + username + ", Level: " + level);
    }
}


