package org.foresthat.example;


/**
 * Config for program
 * @author ForestHat
 */
public interface Config {
    // Variable which show program the maximum number of threads that will be created
    int threadsNumber = 20;

    // Tells the program the maximum number of stored news
    int maxNewsCount = 20;

    // Contains the user wanted types of news
    String[] newsTypes = new String[]{"0", "1", "2", "3"};

    // Url to mongodb
    String mongoUrl = "mongodb://127.0.0.1:27017";

    // Database which keep collection
    String mongoDB = "test";

    // Collection where stored our news
    String mongoCollection = "news";

    // Path to site parser
    String parserPath = "/home/fox/parser";

    // Symbol that separates news
    String newsSplitSymbol = "    ";

    // Symbol which separates title and url
    String splitBetweenTitleAndUrl = "  ";

    // Path to AI
    String aiPath = "cd /home/fox/testis/NewsAnalyzer " +
            "&& source test/bin/activate " +
            "&& python3 start.py ";

    // Symbol which separates unimportant logs and important output
    String splitResultAI = "OUTPUT: ";
}