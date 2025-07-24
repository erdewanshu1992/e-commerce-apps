// src/main/java/com/ecommerce/data/TestDataProviders.java

package com.ecommerce.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.DataProvider;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestDataProviders {

    private static final Logger logger = LogManager.getLogger(TestDataProviders.class);

    @DataProvider(name = "loginData")
    public static Object[][] getLoginData() { // Renamed for clarity and consistency with @Test annotation
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Ensure the path is correct based on where you store login_data.json
            File jsonFile = new File("src/main/resources/testdata/login_data.json");
            logger.info("Attempting to read login data from: {}", jsonFile.getAbsolutePath());

            // Read the JSON file into a List of Maps
            List<Map<String, Object>> jsonData = mapper.readValue(jsonFile,
                    mapper.getTypeFactory().constructCollectionType(List.class, Map.class));

            // Convert the List of Maps to Object[][] for TestNG DataProvider
            List<Object[]> dataList = new ArrayList<>();
            for (Map<String, Object> row : jsonData) {
                dataList.add(new Object[]{
                        row.get("username"),
                        row.get("password"),
                        row.get("level"),
                        row.get("expectedSuccess")
                        // If you also want description: row.get("description")
                });
            }
            logger.info("Successfully loaded {} login data rows from JSON.", dataList.size());
            return dataList.toArray(new Object[0][]); // Convert List<Object[]> to Object[][]
        } catch (IOException e) {
            logger.error("Failed to load login data from JSON file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load login data from JSON file: " + e.getMessage(), e);
        }
    }

    // Add the customerData DataProvider here as well, reading from CSV
    @DataProvider(name = "customerData")
    public static Object[][] getCustomerData() {
        // Implement CSV reading logic here (e.g., using OpenCSV or Apache Commons CSV)
        // For now, if you want to test, you can put the hardcoded customer data here temporarily,
        // but the goal is to read it from customer_data.csv
        return new Object[][] {
                {"CUST001", "John", "Doe", "john.doe@example.com", "123-456-7890", "Active"},
                // ... rest of customer data (ideally read from CSV)
        };
    }
}















/*
// src/main/java/com/ecommerce/data/TestDataProviders.java

package com.ecommerce.data;

import org.testng.annotations.DataProvider;

public class TestDataProviders {

    @DataProvider(name = "loginData")
    public static Object[][] getLoginData() {
        return new Object[][] {
                // {username, password, level, expectedSuccess}
                {"admin", "admin123", "Beginner", true}, // Corrected: "Beginner" as a String
                {"invalid_user", "wrong_password", "Beginner", false}, // Corrected username and added "Beginner"
                {"", "some_password", "Beginner", false}, // Corrected: Added "Beginner" and aligned parameters
                {"admin", "", "Beginner", false}, // Added: Test for empty password (based on screenshot and common scenarios)
                {"admin", "admin123", "Advanced", true}, // Example: Another valid level
                {"admin", "admin123", "InvalidLevel", false} // Example: Test for invalid level
        };
    }

}

 */














/*
// src/main/java/com/ecommerce/utils/TestDataProviders.java

package com.ecommerce.data;

import org.testng.annotations.DataProvider;

public class TestDataProviders {

    @DataProvider(name = "loginData")
    public static Object[][] getLoginData() {
        return new Object[][] {
                {"admin", "admin123", Beginner, true},
                {"invalid_user@example.com", "wrong_password", Beginner, false},
                {"", "some_password", false}
        };
    }

    @DataProvider(name = "customerAccountCreationData")
    public static Object[][] getCustomerAccountCreationData() {
        return new Object[][] {
                {"John", "Doe", "john.doe@example.com", "password123", true},
                {"Jane", "Smith", "jane.smith@example.com", "password123", false} // Example for existing user
        };
    }
}
 */