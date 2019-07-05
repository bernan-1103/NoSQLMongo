/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication3;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.currentDate;
import static com.mongodb.client.model.Updates.set;
import com.mongodb.client.result.UpdateResult;
import java.util.Iterator;
import java.util.function.Consumer;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.conversions.Bson;
/**
 *
 * @author Hecferme
 */
public class JavaApplication3 {

    public static boolean doesCollectionExist(MongoDatabase database, String collectionName)
    {
        boolean result = false;
        MongoIterable<String> collections = database.listCollectionNames();

        Iterator <String> it = collections.iterator();
        while(!result && it.hasNext()){
            if(it.next().equalsIgnoreCase(collectionName))
                result = true;
        }
        return result;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        Consumer<String> action = System.out::println;
        mongoClient.listDatabaseNames().forEach(action);

        // connect to database
        MongoDatabase database = mongoClient.getDatabase("veterinaria");
        // make a Collection if it doesn't exist
        if (!doesCollectionExist(database, "customers"))
            database.createCollection("customers");
        // display all existing collections for current database
        database.listCollectionNames().forEach(action);
        
        // Insert customer
        MongoCollection<Document> collection = database.getCollection("customers");
        Document document = new Document();
        document.put("name", "New Shubham");
        document.put("company", "Baeldung");
        collection.insertOne(document);
        
        // save with update semantics, operating on an existing customer
        Bson filter = eq("name", "Shubham");

        Bson query = combine(set("name", "John"),
				currentDate("lastModified"));
	UpdateResult result = collection.updateOne(filter, query);
        System.out.println("Update with name: " + result.wasAcknowledged());
	System.out.println("Number of Record Modified: " + result.getModifiedCount());
        result = collection.updateOne(Filters.eq("name", "New Shubham"),
                    new Document("$set", new Document("name", "New John"))
            ); // Update the checksum into the database
        System.out.println("Update with name: " + result.wasAcknowledged());
	System.out.println("Number of Record Modified: " + result.getModifiedCount());
    }
    
}
