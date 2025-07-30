package com.ecommerce.services;

import com.ecommerce.utils.ConfigReader;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Service class for interacting with the authentication API.
 */
public class AuthService extends RestClient {

    private static final Logger logger = LogManager.getLogger(AuthService.class);

    public AuthService() {
        // Get API base URL from config.properties
        super(ConfigReader.getProperty("api.base.url", "/api/users"));
        logger.info("AuthService initialized.");
    }

    /**
     * Logs a user in via API and returns the authentication token.
     *
     * @param username The user's username.
     * @param password The user's password.
     * @return The authentication token (e.g., JWT), or null if login fails.
     * @throws RuntimeException if API call fails or token not found.
     */
    public String loginAndGetAuthToken(String username, String password) {
        logger.info("Attempting API login for user: {}", username);
        Map<String, String> loginPayload = new HashMap<>();
        loginPayload.put("username", username);
        loginPayload.put("password", password);

        // Define headers if needed, e.g., Accept application/json
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");

        // Assuming login endpoint is /auth/login
        Response response = post("/auth/login", loginPayload, headers);

        if (response.statusCode() == 200) {
            // Assuming the token is in a field called 'accessToken' or 'token' in the JSON response
            String token = response.jsonPath().getString("accessToken"); // Adjust based on your API response
            if (token == null || token.isEmpty()) {
                token = response.jsonPath().getString("token"); // Try alternative key
            }

            if (token != null && !token.isEmpty()) {
                logger.info("Successfully obtained auth token for user: {}", username);
                return token;
            } else {
                logger.error("Auth token not found in response for user: {}. Response: {}", username, response.body().asString());
                throw new RuntimeException("Auth token not found in API login response.");
            }
        } else {
            logger.error("API login failed for user: {}. Status: {}, Body: {}", username, response.statusCode(), response.body().asString());
            throw new RuntimeException("API login failed. Status: " + response.statusCode());
        }
    }

    /**
     * Registers a new user via API.
     * @param username
     * @param password
     * @param email
     * @return The API response.
     */
    public Response registerUser(String username, String password, String email) {
        logger.info("Attempting to register new user: {}, email: {}", username, email);
        Map<String, String> registerPayload = new HashMap<>();
        registerPayload.put("username", username);
        registerPayload.put("password", password);
        registerPayload.put("email", email);

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");

        // Assuming registration endpoint is /auth/register
        Response response = post("/auth/register", registerPayload, headers);

        if (response.statusCode() == 201) { // 201 Created is typical for successful registration
            logger.info("User registered successfully via API: {}", username);
        } else {
            logger.error("API user registration failed for: {}. Status: {}, Body: {}", username, response.statusCode(), response.body().asString());
        }
        return response;
    }
}