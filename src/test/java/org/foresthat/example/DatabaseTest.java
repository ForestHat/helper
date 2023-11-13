package org.foresthat.example;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    @Test
    void mockTest() {
        Database database = Mockito.mock();

        database.addNews("newsTitle", "newsUrl");
        database.checkExists("newsTitle");
        database.deleteNews("newsTitle", "newsUrl");
        database.getCount();

        Mockito.verify(database).addNews("newsTitle", "newsUrl");
        Mockito.verify(database).checkExists("newsTitle");
        Mockito.verify(database).deleteNews("newsTitle", "newsUrl");
        Mockito.verify(database).getCount();
    }

    @Test
    void realTest() {
        Database db = new Database(Config.mongoUrl, Config.mongoDB, Config.mongoCollection);

        db.addNews("newsTitle3", "newsUrl3");
        db.addNews("newsTitle2", "newsUrl2");
        db.addNews("newsTitle1", "newsUrl1");
        db.addNews("newsTitle", "newsUrl");

        db.checkSize(1);

        assertEquals(1, db.getCount());
        assertTrue(db.checkExists("newsTitle"));

        db.deleteNews("newsTitle", "newsUrl");

        assertFalse(db.checkExists("newsTitle"));
        assertFalse(db.checkExists("unknownTitle"));

        Document doc = db.createDocument("newsTitle", "newsUrl");
        assertTrue(doc.containsKey("newsTitle"));
        assertFalse(doc.containsKey("unknownTitle"));
    }

}