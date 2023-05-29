package main.java;

import java.sql.*;
import java.util.ArrayList;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

class Main {

    private static String queryTotalVentas = "SELECT\n" +
            "  NOM_PAIS,\n" +
            "  SUM(NUMERO_UNIDADES * PRECIO_UNITARIO) AS TOTAL_VENTAS\n" +
            "FROM\n" +
            "  VENTA V\n" +
            "  JOIN SUCURSAL\n" +
            "  ON V.COD_SUC = SUCURSAL.CODIGO_SUC\n" +
            "  JOIN CIUDAD\n" +
            "  ON SUCURSAL.NOM_CIUDAD = CIUDAD.NOM_CIUDAD,\n" +
            "  TABLE(DETALLES)\n" +
            "GROUP BY\n" +
            "  NOM_PAIS";

    static public void main(String[] args) {
        try {
            GenerarEstadisticas();
        } catch (Exception e) {
            System.out.println("No se pudo conectar a la base de datos de MongoDB");
            return;
        }
        System.out.println("Consulta finalizada.");
    } //Fin del main

    public static void GenerarEstadisticas() {
        try {
            Connection oracleDb = ConexionOracle();
            MongoDatabase mongoDb = ConexionMongo();

            Statement sentencia = oracleDb.createStatement();
            ResultSet resultado;
            // Calcular total ventas por pais
            resultado = sentencia.executeQuery(queryTotalVentas);


            // Eliminar collection si ya existe
            if (mongoDb.listCollectionNames().into(new ArrayList<>()).contains("totalventasporpais")) {
                mongoDb.getCollection("totalventasporpais").drop();
            }
            mongoDb.createCollection("totalventasporpais");
            MongoCollection<Document> TotalVentasPorPais = mongoDb.getCollection("totalventasporpais");

            while (resultado.next()) {
                Document document = new Document();
                document.put("nombrepais", resultado.getString("NOM_PAIS"));
                document.put("totalvendidototalvendido", resultado.getInt("TOTAL_VENTAS"));
                TotalVentasPorPais.insertOne(document);
            }
        } catch (Exception e) {
            System.out.println("Fallo al generar estadisticas");
        }
    }

    private static Connection ConexionOracle() {
        Connection conn = null;
        System.out.println("Conexión a la base de datos...");
        try { // Se carga el driver JDBC-ODBC
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (Exception e) {
            System.out.println("No se pudo cargar el driver JDBC");
        }
        try { // Se establece la conexión con la base de datos
            conn = DriverManager.getConnection
                    ("jdbc:oracle:thin:@localhost:1521/XEPDB1", "MATEO", "123");
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("No hay conexión con la base de datos Oracle.");
        }
        return conn;
    }

    private static MongoDatabase ConexionMongo() {
        try {
            String connectionString = "mongodb://localhost:27017";

            MongoClient mongoClient = MongoClients.create(connectionString);
            MongoDatabase database = mongoClient.getDatabase("bd2");

            System.out.println("Conexión exitosa a la base de datos: " + database.getName());
            return database;
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("No hay conexión con la base de datos MongoDB.");
            throw new MongoException("No hay conexión con la base de datos MongoDB.");
        }
    }
} //Fin de la clase


