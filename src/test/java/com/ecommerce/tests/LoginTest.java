package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.LoginPage;
import com.ecommerce.data.TestDataProviders;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    // Option A: Use the DataProvider that reads from JSON
     @Test(dataProvider = "loginData", dataProviderClass = TestDataProviders.class, description = "Verify login with different credentials from JSON")
     public void testLoginFromJson(String username, String password, String level, boolean expectedSuccess) {
         LoginPage loginPage = new LoginPage(driver);
         loginPage.enterUsername(username);
         loginPage.enterPassword(password);
         loginPage.enterLevel(level);
         loginPage.clickLoginButton();

         if (expectedSuccess) {
             Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful for: " + username + ", Level: " + level);
         } else {
             Assert.assertFalse(loginPage.isLoginSuccessful(), "Login should fail for: " + username + ", Level: " + level);
         }
     }

    // Option B: Use the DataProvider that reads from Database
    // Note: The DB data currently doesn't provide 'expectedSuccess'.
    // You'll need to add a column to your DB or derive expectedSuccess in the test.
    @Test(dataProvider = "dbLoginData", dataProviderClass = TestDataProviders.class, description = "Verify login with credentials from Database")
    public void testLoginFromDB(String username, String password, String level) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.enterLevel(level);
        loginPage.clickLoginButton();


        boolean expectedSuccess = isExpectedSuccess(username, password, level);

        if (expectedSuccess) {
            Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful for: " + username + ", Level: " + level);
        } else {
            Assert.assertFalse(loginPage.isLoginSuccessful(), "Login should fail for: " + username + ", Level: " + level);
        }
    }

    private static boolean isExpectedSuccess(String username, String password, String level) {
        boolean expectedSuccess;
        if (username.equals("admin") && password.equals("admin123") && level.equals("Beginner")) {
            expectedSuccess = true;
        } else if (username.equals("test_user") && password.equals("test@123") && level.equals("Intermediate")) {
            expectedSuccess = true; // Based on your DB screenshot's second row
        } else if (username.equals("prod_user") && password.equals("prod@123") && level.equals("Advanced")) {
            expectedSuccess = true; // Based on your DB screenshot's third row
        }
        else {
            expectedSuccess = false; // All other combinations are assumed to fail
        }
        return expectedSuccess;
    }
}
