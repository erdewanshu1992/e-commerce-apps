package com.ecommerce.integrations;

import com.ecommerce.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Base64; // For Basic Auth
import java.util.HashMap;
import java.util.Map;

public class JiraAPI {

    private static final Logger logger = LogManager.getLogger(JiraAPI.class);
    private final String jiraBaseUrl;
    private final String jiraUsername;
    private final String jiraApiToken; // Or password for older versions/basic auth

    public JiraAPI() {
        this.jiraBaseUrl = ConfigReader.getProperty("jira.api.url", "/api/users");
        this.jiraUsername = ConfigReader.getProperty("jira.username", "/api/users");
        this.jiraApiToken = ConfigReader.getProperty("jira.api.token", "/api/users"); // Use API Token for better security
        RestAssured.baseURI = jiraBaseUrl; // Set base URI for RestAssured
        logger.info("JiraAPI initialized with base URL: " + jiraBaseUrl);
    }

    /**
     * Creates a basic issue (bug) in Jira.
     *
     * @param projectKey The project key (e.g., "PROJ").
     * @param summary    Summary of the issue.
     * @param description Detailed description of the issue.
     * @return The response from Jira API.
     */
    public Response createBug(String projectKey, String summary, String description) {
        if (jiraUsername == null || jiraApiToken == null) {
            logger.error("Jira credentials not configured. Cannot create bug.");
            return null;
        }

        // Basic Auth header (Jira uses Basic Auth with username:APIToken)
        String authString = jiraUsername + ":" + jiraApiToken;
        String encodedAuth = Base64.getEncoder().encodeToString(authString.getBytes());
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + encodedAuth);
        headers.put("Accept", "application/json");

        // Construct the JSON payload for creating an issue
        String requestBody = String.format(
                "{"
                        + "\"fields\": {"
                        + "\"project\": {"
                        + "\"key\": \"%s\""
                        + "},"
                        + "\"summary\": \"%s\","
                        + "\"description\": \"%s\","
                        + "\"issuetype\": {"
                        + "\"name\": \"Bug\"" // Or "Bug" or "Task", etc.
                        + "}"
                        + "}"
                        + "}",
                projectKey, summary, description
        );

        logger.info("Attempting to create Jira bug: Summary - {}, Project - {}", summary, projectKey);

        Response response = RestAssured.given()
                .headers(headers)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/rest/api/2/issue"); // Jira API endpoint for creating issues

        if (response.statusCode() == 201) { // 201 Created is success for issue creation
            String issueKey = response.jsonPath().getString("key");
            logger.info("Successfully created Jira bug: {}", issueKey);
        } else {
            logger.error("Failed to create Jira bug. Status Code: {}, Response Body: {}", response.statusCode(), response.body().asString());
        }
        return response;
    }

    // You can add more methods here:
    // public Response getIssueDetails(String issueKey) { ... }
    // public Response addCommentToIssue(String issueKey, String comment) { ... }
    // public Response updateIssueStatus(String issueKey, String transitionId) { ... }
}