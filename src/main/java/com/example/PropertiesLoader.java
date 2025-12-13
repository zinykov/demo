package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    private final Properties properties = new Properties();

    public PropertiesLoader(String resourceFileName) {
        // Try-with-resources ensures the InputStream is closed automatically in Java 7+
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(resourceFileName)) {
            if (input == null) {
                System.err.println("Sorry, unable to find " + resourceFileName);
                return;
            }
            // Load the properties from the InputStream
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
