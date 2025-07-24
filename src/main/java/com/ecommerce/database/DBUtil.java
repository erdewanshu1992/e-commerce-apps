package com.ecommerce.database;

import com.ecommerce.utils.ConfigReader; // Import ConfigReader to get DB properties
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Properties;

public class DBUtil {

    private static final Logger logger = LogManager.getLogger(DBUtil.class);
    // Using ThreadLocal for connection to support parallel test execution
    // where each test might need its own independent DB connection.
    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    /**
     * Establishes a database connection and stores it in a ThreadLocal variable.
     * This method should be called at the beginning of a test suite or method.
     * Database credentials are fetched from ConfigReader.
     *
     * @throws RuntimeException if the database driver is not found or connection fails.
     */
    public static void establishConnection() {
        Connection connection = connectionThreadLocal.get();
        if (connection != null) {
            logger.info("Database connection already established for this thread. Reusing existing connection.");
            return; // Connection already exists, no need to create a new one
        }

        Properties config = ConfigReader.getProperties(); // Get properties from ConfigReader
        String dbUrl = config.getProperty("db.url");
        String dbUsername = config.getProperty("db.username");
        String dbPassword = config.getProperty("db.password");
        String dbDriver = config.getProperty("db.driver");

        if (dbUrl == null || dbUsername == null || dbPassword == null || dbDriver == null) {
            logger.error("Missing database configuration properties. Please check db.url, db.username, db.password, db.driver in config file.");
            throw new RuntimeException("Missing database configuration properties.");
        }

        try {
            // Ensure the JDBC driver is loaded
            Class.forName(dbDriver);
            logger.info("JDBC Driver loaded: {}", dbDriver);

            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            connectionThreadLocal.set(connection); // Store the connection in ThreadLocal
            if (connection != null) {
                logger.info("Successfully connected to the database: {}", dbUrl);
            } else {
                logger.error("Failed to establish database connection to: {}", dbUrl);
                throw new SQLException("DriverManager returned null connection.");
            }
        } catch (SQLException e) {
            logger.error("Database connection error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to connect to database: " + dbUrl + ". Error: " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            logger.error("Database driver not found: {}. Please add the JDBC driver to your classpath.", dbDriver, e);
            throw new RuntimeException("Database driver not found: " + dbDriver, e);
        }
    }

    /**
     * Closes the database connection for the current thread.
     * It also removes the connection from ThreadLocal.
     */
    public static void closeConnection() {
        Connection connection = connectionThreadLocal.get();
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    logger.info("Database connection closed for this thread.");
                } else {
                    logger.debug("Database connection was already closed for this thread.");
                }
            } catch (SQLException e) {
                logger.error("Error closing database connection: {}", e.getMessage(), e);
            } finally {
                connectionThreadLocal.remove(); // Remove connection from ThreadLocal
            }
        } else {
            logger.debug("No database connection found for this thread to close.");
        }
    }

    /**
     * Retrieves the current database connection for the thread.
     *
     * @return The Connection object.
     * @throws IllegalStateException if no connection is established for the current thread.
     */
    private static Connection getConnection() {
        Connection connection = connectionThreadLocal.get();
        if (connection == null) {
            logger.error("No database connection found for the current thread. Call establishConnection() first.");
            throw new IllegalStateException("Database connection not established for current thread.");
        }
        return connection;
    }

    /**
     * Executes an update query (INSERT, UPDATE, DELETE).
     *
     * @param query The SQL query to execute.
     * @return The number of rows affected.
     * @throws RuntimeException if a SQLException occurs during query execution.
     */
    public static int executeUpdate(String query) {
        int rowsAffected = 0;
        try (Statement statement = getConnection().createStatement()) {
            rowsAffected = statement.executeUpdate(query);
            logger.debug("Executed update query: '{}', Rows affected: {}", query, rowsAffected);
        } catch (SQLException e) {
            logger.error("Error executing update query: '{}'. {}", query, e.getMessage(), e);
            throw new RuntimeException("Failed to execute update query: " + query, e);
        }
        return rowsAffected;
    }

    /**
     * Executes a select query.
     *
     * @param query The SQL query to execute.
     * @return A ResultSet containing the query results. The caller is responsible for closing the ResultSet and its Statement.
     * @throws RuntimeException if a SQLException occurs during query execution.
     */
    public static ResultSet executeQuery(String query) {
        try {
            Statement statement = getConnection().createStatement();
            logger.debug("Executing query: '{}'", query);
            return statement.executeQuery(query);
        } catch (SQLException e) {
            logger.error("Error executing query: '{}'. {}", query, e.getMessage(), e);
            throw new RuntimeException("Failed to execute query: " + query, e);
        }
    }

    /**
     * Fetches a single String value from the first column of the first row of a query result.
     *
     * @param query The SQL query to execute.
     * @return The String value, or null if no result is found.
     */
    public static String getSingleStringValue(String query) {
        try (Statement statement = getConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            if (rs.next()) {
                String value = rs.getString(1);
                logger.debug("Fetched single string value for query '{}': {}", query, value);
                return value;
            }
        } catch (SQLException e) {
            logger.error("Error fetching single string value for query '{}'. {}", query, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch single string value: " + query, e);
        }
        return null;
    }

    /**
     * Fetches a single Integer value from the first column of the first row of a query result.
     *
     * @param query The SQL query to execute.
     * @return The Integer value, or null if no result is found.
     */
    public static Integer getSingleIntegerValue(String query) {
        try (Statement statement = getConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            if (rs.next()) {
                int value = rs.getInt(1);
                if (rs.wasNull()) { // Check if the database value was SQL NULL
                    return null;
                }
                logger.debug("Fetched single integer value for query '{}': {}", query, value);
                return value;
            }
        } catch (SQLException e) {
            logger.error("Error fetching single integer value for query '{}'. {}", query, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch single integer value: " + query, e);
        }
        return null;
    }

    // You can add more specific data retrieval methods (e.g., getRowAsMap, getTableAsListofMaps)
    // based on your project's needs.
}