// src/test/java/com/ecommerce/tests/LoginTest.java (Example)
package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.LoginPage;
import com.ecommerce.data.TestDataProviders;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(dataProvider = "loginData", dataProviderClass = TestDataProviders.class, description = "Verify login with different credentials")
    public void testLogin(String username, String password, String level, boolean expectedSuccess) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.enterLevel(level);
        loginPage.clickLoginButton();

        if (expectedSuccess) {
            Assert.assertTrue(loginPage.isPageLoaded(), "Login should be successful for: " + username);
        } else {
            Assert.assertFalse(loginPage.isLoginSuccessful(), "Login should fail for: " + username + " with level " + level);
        }
    }
}













//package com.ecommerce.tests;
//
//
//import com.ecommerce.base.BaseTest;
//import com.ecommerce.pages.LoginPage;
//import org.testng.Assert;
//import org.testng.annotations.Test;
//
//public class LoginTest extends BaseTest {
//
//    @Test
//    public void verifyLoginFlow() {
//        LoginPage loginPage = new LoginPage(driver);
//        loginPage.doLogin("admin", "admin123", "Beginner");
//        Assert.assertTrue(loginPage.isLoginSuccessful());
//        Assert.assertTrue(loginPage.isLoginSuccessful2());
//        // Assert.assertTrue(condition, "message")
//        Assert.assertTrue(loginPage.isLoginSuccessful3(), "Login failed, profile icon not displayed");
//        Assert.assertTrue(loginPage.isLoginSuccessful4(), "Login failed, URL does not contain 'loginLevels'");
//        boolean isProfileIconVisible = loginPage.isLoginSuccessful3();
//        Assert.assertTrue(isProfileIconVisible, "Login failed: Profile icon is not visible after login.");
//
//    }
//}
