package org.foresthat.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateDBTest {
    private final UpdateDB updateDB = new UpdateDB();

    @Test
    void callCommandTest() {
        assertEquals("hello from test 2!", updateDB.callCommand("echo 'hello from test 2!'"));
    }

    @Test
    void checkWishTest() {
        String verdictOne = "1";
        String verdictThree = "3";

        assertTrue(updateDB.checkWish(verdictOne, new String[]{"0", "2", "1"}));
        assertFalse(updateDB.checkWish(verdictThree, new String[]{"0", "1", "2"}));
    }

    @Test
    void addToDatabaseTest() {
        Database database = new Database(Config.mongoUrl, Config.mongoDB, Config.mongoCollection);

        updateDB.addToDatabase("newsTitle999", "newsUrl999", database);
        assertTrue(database.checkExists("newsTitle999"));
        assertFalse(database.checkExists("newsTitle555"));
        assertEquals(1, database.getCount());
    }

    @Test
    void splitNewsTest() {
        String twoNews = "testNews1  url1    testNews2  url2";
        String[] result = new String[]{"testNews1  url1", "testNews2  url2"};

        assertArrayEquals(result, updateDB.splitNews(twoNews));
    }

    @Test
    void splitTitleAndUrlTest() {
        String newsExample = "testNews1  testUrl1";

        assertEquals("testNews1", updateDB.splitTitleAndUrl(newsExample, 0));
        assertEquals("testUrl1", updateDB.splitTitleAndUrl(newsExample, 1));
    }

    @Test
    void buildPathToAITest() {
        String title = "newsTitle";
        String result = "cd /home/fox/testis/NewsAnalyzer " +
                "&& source test/bin/activate " +
                "&& python3 start.py \"newsTitle\"";

        assertEquals(result, updateDB.buildPathToAI(title));
    }

    @Test
    void getVerdictOfAITest() {
        String result = "3";
        String input = "1/1 [==============================] - 0s 415ms/step\n" +
                "OUTPUT: 3";

        assertEquals(result, updateDB.getVerdictOfAI(input));
    }
}