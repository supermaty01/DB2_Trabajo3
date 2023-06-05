package Funciones;

import Conexiones.MongoDB;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class ListarEstadisticas {

    private final String coleccionPais = "totalventasporpais";
    private final String coleccionEtario = "totalventasporcatygedad";

    public String listarEstadisticas() {
        try {
            StringBuilder resultado = new StringBuilder("Total Ventas por Pais\n\n");
            MongoDB mongoDb = new MongoDB();

            MongoCollection<Document> coleccion = mongoDb.conn.getCollection(this.coleccionPais);
            FindIterable<Document> cursor = coleccion.find();

            for (Document doc : cursor) {
                resultado.append(doc.getString("nombrepais")).append(" | ").append(doc.getInteger("totalvendido").toString()).append("\n");
            }

            resultado.append("\n\nTotal Ventas por Categoria y Grupo Etario\n\n").append("Categoria | Grupo Etario | Total Vendido\n");

            coleccion = mongoDb.conn.getCollection(this.coleccionEtario);
            cursor = coleccion.find();

            for (Document doc : cursor) {
                String categoria = doc.getString("nombrecategoria");
                String grupoEtario = doc.getString("grupoetario");
                String totalVendido = doc.getInteger("totalvendido").toString();
                resultado.append(categoria).append(" | ").append(grupoEtario).append(" | ").append(totalVendido).append("\n");
            }

            return resultado.toString();

        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
