package org.foresthat.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A class that contains a method for calling commands
 * @author ForestHat
 * @version 0.1.0
 */
public class Command {
    /**
     * The static method, that calls the system command
     * @param cmd The command to be executed
     * @return The output of the command
     */
    public static String exec(String cmd) {
        // Create new builder for result
        StringBuilder output = new StringBuilder();

        try {
            // Run the command
            Process process = Runtime.getRuntime().exec(new String[]{"bash", "-l", "-c", cmd});
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // Write the command to output variable
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                output.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Returning the output
        return String.valueOf(output);
    }
}