package com.ecommerce.services;

import com.ecommerce.api.models.LoginRequest;
import com.ecommerce.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Service class for interacting with the user-related API endpoints.
 */
public class UserService extends RestClient {

    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final String usersEndpoint;

    /**
     * Performs a login API request.
     * @param request The LoginRequest object containing username, password, and level.
     * @return The RestAssured Response object from the login attempt.
     */
    public Response loginUser(LoginRequest request) {
        logger.info("Attempting API login for user: {} with level: {}", request.getUsername(), request.getLevel());
        return RestAssured.given()
                .baseUri(baseUri) // Ensure base URI is used
                .contentType(ContentType.JSON) // Set content type for request body
                .body(request) // Serialize LoginRequest POJO to JSON
                .when()
                .post(usersEndpoint); // Endpoint for login
    }

    /**
     * Retrieves a user by their username (example GET request).
     * @param username The username to retrieve.
     * @return The RestAssured Response object.
     */
    public Response getUserByUsername(String username) {
        logger.info("Retrieving user by username: {}", username);
        // Assuming your API supports GET /api/users?username=X or /api/users/{username}
        // Let's use query param for this example. Adjust if your API uses path params.
        return RestAssured.given()
                .baseUri(baseUri)
                .queryParam("username", username)
                .when()
                .get(usersEndpoint); // Assuming this endpoint can handle GET with query params
    }



    public UserService() {
        // Option 1: Directly get the base URL and pass it to super()
        // IMPORTANT: ConfigReader.getProperties() must be able to return
        // the Properties object even before 'this' object is fully constructed.
        // Since ConfigReader uses a static ThreadLocal, this is generally safe.
        super(ConfigReader.getProperty("api.base.url", "/api/users"));

        // After super() is called, you can safely access 'this' and other properties.
        String baseUrl = ConfigReader.getProperty("api.base.url", "/api/users"); // Re-read for validation/logging if needed, or remove
        if (baseUrl == null || baseUrl.isEmpty()) { // This check can now happen *after* super() call
            // Although, it's safer if ConfigReader.getProperty handles null/empty
            logger.warn("API Base URL (api.base.url) is null or empty. Ensure config-qa.properties is correct.");
            // Potentially throw exception if strict
        }


        // Get a specific endpoint path
        this.usersEndpoint = ConfigReader.getProperty("api.users.endpoint", "/api/users");
        logger.info("UserService initialized with API base URL: {} and users endpoint: {}", super.baseUri, usersEndpoint);
        // Note: Using super.baseUri or this.baseUri (if this.baseUri was defined)
        // because the baseUri field is in the parent class (RestClient)
    }



    // You can add more methods here like registerUser, updateUser, deleteUser, etc.
}