package com.example;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.io.Resources;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import picocli.CommandLine;
import picocli.CommandLine.Option;

@CommandLine.Command(name = "myApp", description = "Postgesql to cli")
public final class App implements Runnable {
    // Database connection details
    private static String DB_URL;
    private static String USER;
    private static String PASS;
    @Option(names = {"--query", "-q", "/q", }, description = "SQL file path")
    private static String SQL_SCRIPT_FILE;

    private App() {
        PropertiesLoader loader = new PropertiesLoader("application.properties");
        DB_URL = loader.getProperty("database.url");
        USER = loader.getProperty("database.user");
        PASS = loader.getProperty("database.password");
    }

    private static void query_psql() {
        // The driver is automatically loaded in Java 17+
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.println("Connection to PostgreSQL established.");

            // Initialize the ScriptRunner
            ScriptRunner runner = new ScriptRunner(conn);
            
            // Optional Configurations
            runner.setAutoCommit(true);
            runner.setStopOnError(true);
            //runner.setLogWriter(System.out); // Logs the SQL commands executed

            // Load the SQL file from the classpath using MyBatis Resources utility
            try (Reader reader = Resources.getResourceAsReader(SQL_SCRIPT_FILE)) {
                // Run the script
                runner.runScript(reader);
            }

            System.out.println("SQL script executed successfully.");

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error running SQL script: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        query_psql();
    }
    
    public static void main(String[] args) {
        new CommandLine(new App()).execute(args);
    }
}
