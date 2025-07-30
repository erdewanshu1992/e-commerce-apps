package com.ecommerce.data;

import com.ecommerce.database.DBUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.DataProvider;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TestDataProviders {
    private static final Logger logger = LogManager.getLogger(TestDataProviders.class);

    // Existing DataProvider for JSON
    @DataProvider(name = "loginData")
    public static Object[][] getLoginDataFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = null; // Declare InputStream outside try-block
        try {
            String resourcePath = "testdata/login_data.json";
            logger.info("Attempting to read login data from classpath resource: {}", resourcePath);

            is = TestDataProviders.class.getClassLoader().getResourceAsStream(resourcePath);

            if (is == null) {
                logger.error("Resource not found in classpath: {}. Please ensure it's in src/test/resources/data/", resourcePath);
                throw new IOException("Resource not found: " + resourcePath);
            }

            List<Map<String, Object>> jsonData = mapper.readValue(is,
                    mapper.getTypeFactory().constructCollectionType(List.class, Map.class));

            List<Object[]> dataList = new ArrayList<>();
            for (Map<String, Object> row : jsonData) {
                dataList.add(new Object[]{
                        row.get("username"),
                        row.get("password"),
                        row.get("level"),
                        row.get("expectedSuccess")
                });
            }
            logger.info("Successfully loaded {} login data rows from JSON.", dataList.size());
            return dataList.toArray(new Object[0][]);
        } catch (IOException e) {
            logger.error("Failed to load login data from JSON resource '{}': {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load login data from JSON resource: " + e.getMessage(), e);
        } finally {
            if (is != null) {
                try {
                    is.close(); // Close the InputStream
                } catch (IOException e) {
                    logger.error("Error closing InputStream for JSON file: {}", e.getMessage());
                }
            }
        }
    }

    // NEW DataProvider for fetching login credentials from Database
    @DataProvider(name = "dbLoginData")
    public static Object[][] getLoginDataFromDB() {
        // SQL query to fetch data from your 'credentials' table
        // Adjust column names if they differ in your DB table (e.g., 'user_name' instead of 'username')
        String query = "SELECT username, password, level FROM credentials";
        List<Map<String, String>> dbResults = DBUtil.executeQuery(query);

        if (dbResults.isEmpty()) {
            logger.warn("No login data found in the database table 'credentials' using query: {}", query);
            return new Object[][]{}; // Return empty array if no data
        }

        Object[][] data = new Object[dbResults.size()][3]; // 3 columns: username, password, level

        for (int i = 0; i < dbResults.size(); i++) {
            Map<String, String> row = dbResults.get(i);
            // Ensure keys match your database column names EXACTLY (case-sensitive if DB is)
            data[i][0] = row.get("username");
            data[i][1] = row.get("password");
            data[i][2] = row.get("level");
            // Note: This DB query doesn't have 'expectedSuccess'. You'll need to derive it
            // based on the username/password/level in your test or add a column to your DB.
            // For now, the test method will need to handle this.
            // If you add an 'expected_success' column to your DB, you can fetch it:
            // data[i][3] = Boolean.parseBoolean(row.get("expected_success"));
        }
        logger.info("Successfully loaded {} login data rows from Database.", dbResults.size());
        return data;
    }

    // Existing DataProvider for customer data (if reading from CSV)
    @DataProvider(name = "customerData")
    public static Object[][] getCustomerData() {
        // Implement CSV reading logic here
        return new Object[][] {
                {"CUST001", "John", "Doe", "john.doe@example.com", "123-456-7890", "Active"},
                // ...
        };
    }
}