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
import java.util.Iterator;
import java.util.function.Consumer;
import static javaapplication3.JavaApplication3.doesCollectionExist;
import org.bson.Document;

public class GetDataBases {

    private MongoClient mongoClient;
    private MongoDatabase database;
    private Document document;

    public static boolean doesCollectionExist(MongoDatabase database, String collectionName) {
        boolean result = false;
        MongoIterable<String> collections = database.listCollectionNames();

        Iterator<String> it = collections.iterator();
        while (!result && it.hasNext()) {
            if (it.next().equalsIgnoreCase(collectionName)) {
                result = true;
            }
        }
        return result;
    }
    

    public void Conexion() {
           mongoClient = new MongoClient("localhost", 27017);
           Consumer<String> action = System.out::println;
           mongoClient.listDatabaseNames().forEach(action);
    }
    
    public void getDataBase(String nomData){
     database = mongoClient.getDatabase(nomData);
    }
    
    public void getColletions(String nomColletion){ 
            if (!doesCollectionExist(database, nomColletion)){
                createColletion(nomColletion);
            }else{ 
                database.getCollection(nomColletion);
            }
            
        
    }
 
    
    public void createColletion(String nomColletion){
        database.createCollection(nomColletion);
         System.out.println(database.toString());
    }
    
    public void insertColletion(String nomColletion, String nombre){
        MongoCollection<Document> collection = database.getCollection(nomColletion);
        document = new Document();
        document.put("name", nombre);
        collection.insertOne(document);
    }

}
