package com.ecommerce.services;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * A generic REST client using RestAssured for common HTTP methods.
 * Provides a base for specific service classes.
 */
public class RestClient {

    private static final Logger logger = LogManager.getLogger(RestClient.class);
    protected final String baseUri; // Made protected to be accessible by subclasses

    public RestClient(String baseUri) {
        if (baseUri == null || baseUri.trim().isEmpty()) {
            throw new IllegalArgumentException("Base URI cannot be null or empty for RestClient.");
        }
        this.baseUri = baseUri;
        // RestAssured.baseURI is often set here or implicitly by RestAssured if used with given().when().then()
        // No need to set RestAssured.baseURI globally if you always use given().baseUri(baseUri) or if baseUri is passed
        logger.info("Initialized RestClient with base URI: {}", baseUri);
    }

    /**
     * Sends a GET request with headers.
     * @param path The path relative to the base URI.
     * @param headers Map of headers to include in the request.
     * @return The RestAssured Response object.
     */
    public Response get(String path, Map<String, String> headers) {
        logger.info("Sending GET request to: {}{} with headers.", baseUri, path);
        return RestAssured.given()
                .baseUri(baseUri)
                .headers(headers)
                .when()
                .get(path);
    }

    /**
     * Sends a POST request with a JSON body and headers.
     * @param path The path relative to the base URI.
     * @param body The request body (will be serialized to JSON).
     * @param headers Map of headers to include in the request.
     * @return The RestAssured Response object.
     */
    public Response post(String path, Object body, Map<String, String> headers) {
        logger.info("Sending POST request to: {}{} with body: {}", baseUri, path, body != null ? body.getClass().getSimpleName() : "null");
        return RestAssured.given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .headers(headers)
                .body(body)
                .when()
                .post(path);
    }

    /**
     * Sends a PUT request with a JSON body and headers.
     * @param path The path relative to the base URI.
     * @param body The request body (will be serialized to JSON).
     * @param headers Map of headers to include in the request.
     * @return The RestAssured Response object.
     */
    public Response put(String path, Object body, Map<String, String> headers) {
        logger.info("Sending PUT request to: {}{} with body: {}", baseUri, path, body != null ? body.getClass().getSimpleName() : "null");
        return RestAssured.given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .headers(headers)
                .body(body)
                .when()
                .put(path);
    }

    /**
     * Sends a DELETE request with headers.
     * @param path The path relative to the base URI.
     * @param headers Map of headers to include in the request.
     * @return The RestAssured Response object.
     */
    public Response delete(String path, Map<String, String> headers) {
        logger.info("Sending DELETE request to: {}{}", baseUri, path);
        return RestAssured.given()
                .baseUri(baseUri)
                .headers(headers)
                .when()
                .delete(path);
    }

    // Add more specialized methods as needed (e.g., for form-data, XML, authentication)
}