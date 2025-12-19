package com.example;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.io.Resources;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@CommandLine.Command(name = "App", mixinStandardHelpOptions = true, version = "1.0", description = "Postgesql to cli")
public final class App implements Callable<Integer> {
    // Database connection details
    private String DB_URL;
    private String USER;
    private String PASS;
    @Option(names = {"--query", "-q", "/q", }, description = "SQL file path")
    private String SQL_SCRIPT_FILE;

    @Spec CommandSpec spec;

    public App() {
        PropertiesLoader loader = new PropertiesLoader("application.properties");
        DB_URL = loader.getProperty("database.url");
        USER = loader.getProperty("database.user");
        PASS = loader.getProperty("database.password");
    }

    private void query_psql() {
        // The driver is automatically loaded in Java 17+
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            spec.commandLine().getOut().println("Connection to PostgreSQL established.");

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

            spec.commandLine().getOut().println("SQL script executed successfully.");

        } catch (SQLException e) {
            spec.commandLine().getErr().println("Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            spec.commandLine().getErr().println("Error running SQL script: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Integer call() {
        if (SQL_SCRIPT_FILE == null) {
            spec.commandLine().getOut().println("No arguments provided. Displaying help.");
            spec.commandLine().usage(spec.commandLine().getOut());
            return 0;
        }
        query_psql();
        return 0;
    }
    
    public static void main(String[] args) {
        System.exit(new CommandLine(new App()).execute(args));
    }
}
