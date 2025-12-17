package com.example;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Unit test for simple App.
 */
class AppTest {
    @Test
    void testVoidOption() {
        App app = new App();
        CommandLine cmd = new CommandLine(app);

        StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        int exitCode = cmd.execute();

        System.out.println(sw.toString());
        
        assertEquals(0, exitCode);
        assertTrue(sw.toString().contains("No arguments provided. Displaying help."));
    }
    @Test
    void testHelpOption() {
        App app = new App();
        CommandLine cmd = new CommandLine(app);

        StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        int exitCode = cmd.execute("--help");

        System.out.println(sw.toString());
        
        assertEquals(0, exitCode);
        assertTrue(sw.toString().contains("Usage: App"));
    }

    @Test
    void testQuery() {
        App app = new App();
        CommandLine cmd = new CommandLine(app);

        StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));
        cmd.setErr(new PrintWriter(sw));

        int exitCode = cmd.execute("--query","SELECT_products.sql");

        System.out.println(sw.toString());
        
        assertEquals(0, exitCode);
        assertTrue(sw.toString().contains("Connection to PostgreSQL established."));
    }
}