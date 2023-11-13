package org.foresthat.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import java.util.LinkedList;


/**
 * Class that implemented a basic actions with mongodb
 * @author ForestHat
 * @version 0.1.0
 */
public class Database {
    // Create a mongodb collection
    private final MongoCollection<Document> mongoCollection;

    /**
     * Default constructor
     * @param url Url to mongodb database
     * @param db The name of db
     * @param collection The collection in db
     */
    public Database(String url, String db, String collection) {
        MongoClient mongoClient = MongoClients.create(url);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(db);
        this.mongoCollection = mongoDatabase.getCollection(collection);
    }

    /**
     * Method which create document (with arguments) and insert to collection
     * @param newsTitle The title of news
     * @param newsUrl The url to news
     */
    public void addNews(String newsTitle, String newsUrl) {
        mongoCollection.insertOne(createDocument(newsTitle, newsUrl));
    }

    /**
     * Method which delete document by provided arguments
     * @param newsTitle The title of news
     * @param newsUrl The url to news
     */
    public void deleteNews(String newsTitle, String newsUrl) {
        mongoCollection.deleteOne(createDocument(newsTitle, newsUrl));
    }

    /**
     * Method which create document by provided title and url
     * @param newsTitle The title of news
     * @param newsUrl The url to news
     * @return Created document
     */
    public Document createDocument(String newsTitle, String newsUrl) {
        Document document = new Document();

        // Puts the provided arguments
        document.put("newsTitle", newsTitle);
        document.put("newsUrl", newsUrl);

        return document;
    }

    /**
     * Method which gets the count of documents in database
     * @return Count of documents in database
     */
    public long getCount() {
        return mongoCollection.countDocuments();
    }

    /**
     * Method which deletes first news in database, if they many than it is necessary
     * @param maxNewsCount Sets the quantity of possible news items
     */
    public void checkSize(int maxNewsCount) {
        /*
            If in database much news then delete first news,
            because the latest (newest) news added in the end of database
        */
        if (getCount() > maxNewsCount) {
            // Get all news
            FindIterable<Document> allNews = mongoCollection.find();

            // Create new linked list
            LinkedList<Document> linkedList = new LinkedList<>();

            // Adding each element in linked list
            for (Document oneNews : allNews) {
                linkedList.add(oneNews);
            }

            // Until we get the required amount, it deletes the documents
            while (linkedList.size() > maxNewsCount) {
                mongoCollection.deleteOne(linkedList.removeFirst());
            }
        }
    }

    /**
     * Checks if title exists in database
     * @param newsTitle The title of news
     * @return News entry
     */
    public boolean checkExists(String newsTitle) {
        // Add the new document with title of news
        Document searchQuery = new Document();
        searchQuery.put("newsTitle", newsTitle);

        // Create cursor for check existing
        FindIterable<Document> cursor = mongoCollection.find(searchQuery);

        return cursor.first() != null;
    }
}