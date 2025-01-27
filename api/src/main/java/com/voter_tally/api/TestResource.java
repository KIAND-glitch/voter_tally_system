package com.voter_tally.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

@WebServlet(name = "test", urlPatterns = "/test")
public class TestResource extends HttpServlet {
    private static final String PROPERTY_JDBC_URI = "jdbc.uri";
    private static final String PROPERTY_JDBC_USERNAME = "jdbc.username";
    private static final String PROPERTY_JDBC_PASSWORD = "jdbc.password";
    private static final String SQL_GET_TEST = "SELECT * FROM app.test;";

    @Override
    public void init() throws ServletException {
        // load the driver
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("PostgreSQL Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL Driver not found. Ensure the JDBC driver is in your classpath.");
            throw new RuntimeException(e);
        }
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Entering doGet method...");

        String jdbcUri = System.getProperty(PROPERTY_JDBC_URI);
        String jdbcUsername = System.getProperty(PROPERTY_JDBC_USERNAME);
        String jdbcPassword = System.getProperty(PROPERTY_JDBC_PASSWORD);

        System.out.println("Database URI: " + jdbcUri);
        System.out.println("Database Username: " + jdbcUsername);

        try (Connection connection = DriverManager.getConnection(jdbcUri, jdbcUsername, jdbcPassword)) {
            System.out.println("Database connection established successfully.");

            try (PreparedStatement statement = connection.prepareStatement(SQL_GET_TEST)) {
                System.out.println("Executing SQL query: " + SQL_GET_TEST);
                ResultSet results = statement.executeQuery();

                if (results.next()) {
                    String testValue = results.getString("test_value");
                    System.out.println("Fetched value from database: " + testValue);
                    resp.getWriter().println(testValue);
                } else {
                    System.out.println("No results found.");
                    resp.getWriter().println("No test data found.");
                }

            } catch (SQLException e) {
                System.err.println("Error executing SQL query.");
                e.printStackTrace();
                resp.getWriter().println("Error executing query: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Error establishing database connection.");
            e.printStackTrace();
            resp.getWriter().println("Error connecting to the database: " + e.getMessage());
        }

        System.out.println("Exiting doGet method...");
    }
}
