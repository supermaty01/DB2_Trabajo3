package Conexiones;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDB {

    private String baseDatos = "bd2";
    private String url = "mongodb://localhost:27017";
    public MongoDatabase conn;


    public MongoDB() throws Exception {
        this.conexionMongo();
    }


    public void conexionMongo() throws Exception {
        try {
            MongoClient mongoClient = MongoClients.create(url);
            this.conn = mongoClient.getDatabase(baseDatos);
        } catch (Exception e) {
            throw new Exception("No hay conexión con la base de datos MongoDB.");
        }
    }
}
