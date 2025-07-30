package com.ecommerce.services;

import com.ecommerce.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Service class for interacting with the product API.
 */
public class ProductService extends RestClient {

    private static final Logger logger = LogManager.getLogger(ProductService.class);
    private String authToken; // To hold the authentication token for subsequent requests

    public ProductService(String authToken) {
        super(ConfigReader.getProperty("api.base.url", "/api/users"));
        this.authToken = authToken; // Authentication token obtained from AuthService
        logger.info("ProductService initialized with token.");
    }

    /**
     * Retrieves all products from the API.
     *
     * @return The API response containing product list.
     */
    public Response getAllProducts() {
        logger.info("Retrieving all products via API.");
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + authToken); // Assuming Bearer token authentication
        headers.put("Accept", "application/json");
        return get("/products", headers); // Assuming /products endpoint, and get method with headers
    }

    // Overload for get with headers (add this to RestClient as well or ensure its usage here)
    // For now, let's assume RestClient's get() can take headers or ProductService prepares them.
    // Let's add an overloaded `get` method to RestClient.
    @Override
    public Response get(String path, Map<String, String> headers) {
        logger.info("Sending GET request to: {}{} with headers.", baseUri, path);
        return RestAssured.given()
                .baseUri(baseUri)
                .headers(headers)
                .when()
                .get(path);
    }

    /**
     * Adds a new product via API.
     * @param productPayload Map representing the product data (e.g., name, price, description).
     * @return The API response.
     */
    public Response addProduct(Map<String, Object> productPayload) {
        logger.info("Adding new product via API.");
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + authToken);
        headers.put("Accept", "application/json");

        Response response = post("/products", productPayload, headers); // Assuming /products endpoint for POST

        if (response.statusCode() == 201) {
            logger.info("Product added successfully via API: {}", productPayload.get("name"));
        } else {
            logger.error("Failed to add product via API. Status: {}, Body: {}", response.statusCode(), response.body().asString());
        }
        return response;
    }

    // Add more product-related API calls like updateProduct, deleteProduct, getProductById etc.
}