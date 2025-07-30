package com.ecommerce.database;

import com.ecommerce.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DBUtil {

    private static final Logger logger = LogManager.getLogger(DBUtil.class);
    private static Connection connection = null;
    private static Statement statement = null;
    private static ResultSet resultSet = null;

    public static void establishConnection() {
        if (connection == null) {
            try {
                Properties props = ConfigReader.getProperties();
                String dbEnabled = props.getProperty("db.enabled");
                if (!"true".equalsIgnoreCase(dbEnabled)) {
                    logger.info("Database connection not enabled (db.enabled=false). Skipping connection.");
                    return;
                }

                String dbUrl = props.getProperty("db.url");
                String dbUsername = props.getProperty("db.username");
                String dbPassword = props.getProperty("db.password");
                String dbDriver = props.getProperty("db.driver");

                Class.forName(dbDriver);
                logger.info("Database driver loaded: {}", dbDriver);

                connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                logger.info("Database connection established successfully to: {}", dbUrl);

            } catch (ClassNotFoundException e) {
                logger.error("JDBC Driver not found: {}", e.getMessage(), e);
                throw new RuntimeException("JDBC Driver not found. Please ensure it's in your classpath.", e);
            } catch (SQLException e) {
                logger.error("Failed to establish database connection: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to establish database connection.", e);
            }
        }
    }

    public static void closeConnection() {
        try {
            if (resultSet != null) {
                resultSet.close();
                logger.debug("ResultSet closed.");
            }
            if (statement != null) {
                statement.close();
                logger.debug("Statement closed.");
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
                logger.info("Database connection closed.");
            }
        } catch (SQLException e) {
            logger.error("Error closing database resources: {}", e.getMessage(), e);
            throw new RuntimeException("Error closing database resources.", e);
        }
    }

    /**
     * Executes a SQL SELECT query and returns the results as a List of Maps.
     * Each Map represents a row, with column names as keys.
     * @param query The SQL SELECT query string.
     * @return A List of Maps, where each Map represents a row from the query result.
     * @throws RuntimeException if a SQLException occurs.
     */
    public static List<Map<String, String>> executeQuery(String query) {
        List<Map<String, String>> resultList = new ArrayList<>();
        try {
            establishConnection(); // Ensure the connection is open
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query); // Execute query and get ResultSet

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Iterate through the ResultSet and build the List<Map<String, String>>
            while (resultSet.next()) {
                Map<String, String> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String columnValue = resultSet.getString(i); // Get string value for the column
                    row.put(columnName, columnValue);
                }
                resultList.add(row);
            }
            logger.debug("Query executed successfully: '{}'. Rows returned: {}", query, resultList.size());
        } catch (SQLException e) {
            logger.error("Error executing query: '{}'. Message: {}", query, e.getMessage(), e);
            throw new RuntimeException("Failed to execute query: " + query, e);
        } finally {
            // IMPORTANT: Close the ResultSet and Statement in the finally block
            // The connection should stay open as it's managed by the class level 'connection' static variable
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null; // Clear for next use
                }
                if (statement != null) {
                    statement.close();
                    statement = null; // Clear for next use
                }
            } catch (SQLException e) {
                logger.error("Error closing resultSet or statement after query: {}", e.getMessage());
            }
        }
        return resultList; // Return the processed List<Map<String, String>>
    }

    /**
     * Executes a SQL INSERT, UPDATE, or DELETE query.
     * Returns the number of rows affected.
     * @param query The SQL INSERT/UPDATE/DELETE query string.
     * @return The number of rows affected by the query.
     * @throws RuntimeException if a SQLException occurs.
     */
    public static int executeUpdate(String query) {
        int rowsAffected = 0;
        try {
            establishConnection();
            statement = connection.createStatement();
            rowsAffected = statement.executeUpdate(query);
            logger.info("Update query executed successfully: '{}'. Rows affected: {}", query, rowsAffected);
        } catch (SQLException e) {
            logger.error("Error executing update query: '{}'. Message: {}", query, e.getMessage(), e);
            throw new RuntimeException("Failed to execute update query: " + query, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
            } catch (SQLException e) {
                logger.error("Error closing statement after update query: {}", e.getMessage());
            }
        }
        return rowsAffected;
    }
}