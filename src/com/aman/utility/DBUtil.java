package com.aman.utility;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                String driverName = "com.mysql.cj.jdbc.Driver";
                String connectionString = "jdbc:mysql://localhost:3306/trainbook";
                String username = "root"; // your MySQL username
                String password = "root123"; // your MySQL password

                Class.forName(driverName);
                connection = DriverManager.getConnection(connectionString, username, password);
                System.out.println("✅ Connected to MySQL Successfully!");
            }
        } catch (Exception e) {
            System.out.println("❌ Database Connection Failed: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔒 Connection Closed.");
            }
        } catch (Exception e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
