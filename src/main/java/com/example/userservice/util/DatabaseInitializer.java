package com.example.userservice.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
public class DatabaseInitializer {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @PostConstruct
    public void initialize() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            // Check if the 'users' table exists
            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet tables = dbMetaData.getTables(null, null, "users", null);
            if (!tables.next()) {
                // Create the users table if it does not exist
                String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                        + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                        + "address VARCHAR(50), "
                        + "city VARCHAR(50), "
                        + "country VARCHAR(50), "
                        + "date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                        + "email VARCHAR(50) NOT NULL, "
                        + "gender VARCHAR(10), "
                        + "last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
                        + "name VARCHAR(50) NOT NULL, "
                        + "password VARCHAR(255) NOT NULL, "
                        + "phone_number VARCHAR(20), "
                        + "postal_code VARCHAR(10), "
                        + "state VARCHAR(50)"
                        + ")";
                statement.executeUpdate(createTableSQL);
                System.out.println("Table 'users' created.");
            }

            // Check if the unique index exists on the 'email' column
            ResultSet resultSet = dbMetaData.getIndexInfo(null, null, "users", true, false);
            boolean uniqueEmailIndexExists = false;
            while (resultSet.next()) {
                String indexName = resultSet.getString("INDEX_NAME");
                if ("idx_unique_email".equalsIgnoreCase(indexName)) {
                    uniqueEmailIndexExists = true;
                    break;
                }
            }

            // Add the unique index if it does not exist
            if (!uniqueEmailIndexExists) {
                String addIndexSQL = "CREATE UNIQUE INDEX idx_unique_email ON users (email(50))";
                statement.executeUpdate(addIndexSQL);
                System.out.println("Unique index 'idx_unique_email' added.");
            } else {
                System.out.println("Unique index 'idx_unique_email' already exists.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
