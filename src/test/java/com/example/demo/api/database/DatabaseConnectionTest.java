package com.example.demo.api.database;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DatabaseConnectionTest {
    @Test
    void checkDatabases_exists()
    {
        Connection connection;
        try{
            String url      = "jdbc:postgresql://localhost:5432/news_database";
            String username = "postgres";
            String password = "07022005";

            connection = DriverManager.getConnection(url, username, password);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(connection, "Failed to connection to the database");
    }
}
