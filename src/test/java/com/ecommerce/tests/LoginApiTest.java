package com.ecommerce.tests;

import com.ecommerce.api.models.LoginRequest;
import com.ecommerce.api.models.UserResponse;
import com.ecommerce.services.UserService;
import com.ecommerce.utils.ConfigReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import java.util.Properties;
import static org.hamcrest.Matchers.*;



public class LoginApiTest {

    private static final Logger logger = LogManager.getLogger(LoginApiTest.class);
    private UserService userService;
    private String apiUser;
    private String apiPass;

    @BeforeClass
    public void setupApi() {
        // Ensure ConfigReader is initialized if this test runs independently or before BaseTest's @BeforeSuite
        // If it runs as part of a suite where BaseTest's @BeforeSuite runs, ConfigReader is already initialized.
        // It's safer to ensure it's initialized here if this is a standalone API test.
        ConfigReader.init_prop("qa"); // Assumes 'qa' environment for API tests

        userService = new UserService();
        Properties props = ConfigReader.getProperties();
        apiUser = props.getProperty("api.username");
        apiPass = props.getProperty("api.password");
        logger.info("API Test setup complete. Using API user: {}", apiUser);
    }

    // DataProvider for API login tests
    @DataProvider(name = "apiLoginData")
    public Object[][] getApiLoginData() {
        // You would typically load this from a JSON file, CSV, or a database
        // For demonstration, hardcoding some valid and invalid examples.
        // Assuming 'admin', 'admin123', 'Advanced' from your Postman output is a valid combo.
        return new Object[][]{
                // Valid credentials from your Postman output
                {new LoginRequest("admin", "admin123", "Advanced"), true, "Valid admin login"},
                // Example of invalid password for admin
                {new LoginRequest("admin", "wrongpass", "Advanced"), false, "Invalid password for admin"},
                // Example of unknown user
                {new LoginRequest("unknownuser", "pass123", "Beginner"), false, "Unknown user login"},
                // If your API supports different levels for the same user, test that
                {new LoginRequest("test_user", "test@123", "Intermediate"), true, "Valid test_user login"},
        };
    }

    @Test(dataProvider = "apiLoginData", description = "Verify user login via API")
    public void testApiUserLogin(LoginRequest loginRequest, boolean expectedSuccess, String testCaseDescription) {
        logger.info("Running API login test: {}", testCaseDescription);

        Response response = userService.loginUser(loginRequest);

        logger.info("API Login Response Status: {}", response.getStatusCode());
        logger.info("API Login Response Body: {}", response.getBody().asString());

        if (expectedSuccess) {
            // Assertions for a successful login
            response.then()
                    //.statusCode(200) // Assuming 200 OK for successful login
                    .statusCode(201) // Assuming 200 OK for successful login
                    .contentType(ContentType.JSON); // Ensure response is JSON

            // Deserialize the response body into your UserResponse POJO
            UserResponse userResponse = response.as(UserResponse.class);

            Assert.assertNotNull(userResponse, "User response should not be null");
            Assert.assertEquals(userResponse.getUsername(), loginRequest.getUsername(), "Username in response should match request");
            Assert.assertEquals(userResponse.getLevel(), loginRequest.getLevel(), "Level in response should match request");
            Assert.assertEquals(userResponse.getExpectedSuccess(), 1, "expectedSuccess should be 1 for successful login");
            logger.info("API Login successful for user: {}", loginRequest.getUsername());

        } else {
            response.then()
                    .statusCode(allOf(greaterThanOrEqualTo(400), lessThan(500))) // Use allOf with Hamcrest matchers
                    .contentType(ContentType.JSON);

            // You might also assert on specific error messages in the response body
            // e.g., Assert.assertTrue(response.getBody().asString().contains("Invalid credentials"));
            logger.warn("API Login failed as expected for user: {}", loginRequest.getUsername());
        }
    }

    // Example of using the GET method (if your API supports it)
    @Test(description = "Verify retrieving user data via API GET request")
    public void testGetUserByUsername() {
        // Assuming 'admin' user exists and is retrievable by GET /api/users?username=admin
        String testUsername = "admin";
        Response response = userService.getUserByUsername(testUsername);

        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON);

        UserResponse userResponse = response.as(UserResponse.class);
        Assert.assertNotNull(userResponse, "User response should not be null");
        Assert.assertEquals(userResponse.getUsername(), testUsername, "Retrieved username should match requested username");
        logger.info("Successfully retrieved user: {}", testUsername);
    }
}