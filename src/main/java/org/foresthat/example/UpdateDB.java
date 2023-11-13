package org.foresthat.example;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class, which launches the parser and AI
 * @author ForestHat
 * @version 0.1.0
 */
public class UpdateDB {

    /**
     * If you want to run the program, use that method
     */
    public void run() {
        // Create new object of database
        Database database = new Database(Config.mongoUrl, Config.mongoDB, Config.mongoCollection);

        // Get output of parser
        String parserOutput = callCommand(Config.parserPath);

        // Receiving split news
        String[] newsFromParser = splitNews(parserOutput);

        // Create new ExecutorService for multithreading
        ExecutorService executorService = Executors.newFixedThreadPool(Config.threadsNumber);

        // Create stream for each news
        for (int i = 0; i < newsFromParser.length; i++) {
            final int index = i; // Index means the news index in newsFromParser

            // Create thread
            executorService.execute(() -> {
                // newsClass contains title and url of news
                String newsClass = newsFromParser[index];

                // Get title and url
                String newsTitle = splitTitleAndUrl(newsClass, 0);
                String newsUrl = splitTitleAndUrl(newsClass, 1);

                // Get the AI opinion
                String ai = callCommand(buildPathToAI(newsTitle));

                // Verify, if user want to see it, if yes, then add news to db
                if (checkWish(getVerdictOfAI(ai), Config.newsTypes)) {
                    addToDatabase(newsTitle, newsUrl, database);
                }

            });
        }

        // Shutdown the service
        executorService.shutdown();

        // Check size
        database.checkSize(Config.maxNewsCount);
    }

    /**
     * Run the command, and then return output
     * @param cmd Command to launch
     * @return Command output
     */
    public String callCommand(String cmd) {
        return Command.exec(cmd);
    }

    /**
     * Check user wishes, if yes, then add to database
     * @param aiVerdict Output of AI
     * @param recommendedTypes User preferred types of news
     * @return Preferred (true), or not (false)
     */
    public boolean checkWish(String aiVerdict, String[] recommendedTypes) {
        for (String wish : recommendedTypes) {
            if (Objects.equals(wish, aiVerdict)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method which check existing of news and adding to database
     * @param newsTitle The title of news
     * @param newsUrl The url to news
     * @param database Where add
     */
    public void addToDatabase(String newsTitle, String newsUrl, Database database) {
        if (!(database.checkExists(newsTitle))) {
            database.addNews(newsTitle, newsUrl);
        }
    }

    /**
     * Method, which split the news
     * @param line Parser output
     * @return News and urls
     */
    public String[] splitNews(String line) {
        return line.split(Config.newsSplitSymbol);
    }

    /**
     * Method, which split title and url
     * @param line splitNews output
     * @param index Title - 0, url - 1
     * @return Title or url, depends on index
     */
    public String splitTitleAndUrl(String line, int index) {
        return line.split(Config.splitBetweenTitleAndUrl)[index];
    }

    /**
     * Create path to AI
     * @param newsTitle News title, from splitTitleAndUrl
     * @return Path to AI with title
     */
    public String buildPathToAI(String newsTitle) {
        return Config.aiPath + "\"" + newsTitle + "\"";
    }

    /**
     * Method which return AI verdict
     * @param aiOutput Output of AI
     * @return AI verdict
     */
    public String getVerdictOfAI(String aiOutput) {
        return aiOutput.split(Config.splitResultAI)[1];
    }
}
