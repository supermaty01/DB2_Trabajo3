package Funciones;

import Conexiones.MongoDB;
import Conexiones.Oracle;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GenerarEstadisticas {

    private String queryPais = """
            SELECT
              NOM_PAIS,
              SUM(NUMERO_UNIDADES * PRECIO_UNITARIO) AS TOTAL_VENTAS
            FROM
              VENTA V
              JOIN SUCURSAL
              ON V.COD_SUC = SUCURSAL.CODIGO_SUC
              JOIN CIUDAD
              ON SUCURSAL.NOM_CIUDAD = CIUDAD.NOM_CIUDAD,
              TABLE(DETALLES)
            WHERE
                V.ESTADO = 'No procesada'
            GROUP BY
              NOM_PAIS
            """;
    private String queryEtario = """
            SELECT
                CATEGORIA.NOMBRE_CATEGORIA,
                CLIENTE.GRUPO_ETARIO,
                SUM(NUMERO_UNIDADES * PRECIO_UNITARIO) AS TOTAL_VENTAS
            FROM
                VENTA V
                JOIN CLIENTE
                ON V.PASAPORTE = CLIENTE.PASAPORTE JOIN PRODUCTO
                ON CODIGO_PRO = PRODUCTO.CODIGO_PRO
                JOIN CATEGORIA
                ON PRODUCTO.CODIGO_CAT = CATEGORIA.CODIGO_CAT,
                TABLE(DETALLES)
            WHERE
                V.ESTADO = 'No procesada'
            GROUP BY
                CATEGORIA.NOMBRE_CATEGORIA, CLIENTE.GRUPO_ETARIO
                """;
    private String updateProcesados = "UPDATE VENTA  SET ESTADO = 'Procesada' WHERE ESTADO = 'No procesada'";

    private String coleccionPais = "totalventasporpais";
    private String coleccionEtario = "totalventasporcatygedad";


    public void generarEstadisticas() {
        try {
            ResultadosEstadisticas resultadosPais = generarPais();
            ResultadosEstadisticas resultadosEtario = generarEtario();
            System.out.println(resultadosPais.getTexto() + resultadosEtario.getTexto());
            actualizarProcesados();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private ResultadosEstadisticas generarPais() throws Exception {
        Oracle oracle = new Oracle();
        MongoDB mongoDb = new MongoDB();

        Statement sentencia = oracle.conn.createStatement();
        ResultSet resultado = sentencia.executeQuery(this.queryPais);

        long antes = 0;
        long despues = 0;
        long actualizados = 0;
        long noActualizados = 0;
        long creados = 0;

        if (!mongoDb.conn.listCollectionNames().into(new ArrayList<>()).contains(this.coleccionPais)) {
            mongoDb.conn.createCollection(this.coleccionPais);
            MongoCollection<Document> coleccion = mongoDb.conn.getCollection(this.coleccionPais);

            int contador = 0;
            while (resultado.next()) {
                contador++;
                Document document = new Document();
                document.put("nombrepais", resultado.getString("NOM_PAIS"));
                document.put("totalvendido", resultado.getInt("TOTAL_VENTAS"));
                coleccion.insertOne(document);
            }
            despues = contador;
            creados = contador;
        } else {
            MongoCollection<Document> coleccion = mongoDb.conn.getCollection(this.coleccionPais);

            // Documentos antes
            antes = coleccion.countDocuments();

            while (resultado.next()) {
                Document searchQuery = new Document();
                searchQuery.put("nombrepais", resultado.getString("NOM_PAIS"));
                long existeEstadistica = coleccion.countDocuments(searchQuery);
                if (existeEstadistica > 0) {
                    // Actualizar documento
                    actualizados++;
                    FindIterable<Document> cursor = coleccion.find(searchQuery);
                    Document document = new Document();
                    document.put("totalvendido", resultado.getInt("TOTAL_VENTAS") + cursor.first().getInteger("totalvendido"));
                    Document updateObject = new Document();
                    updateObject.put("$set", document);

                    coleccion.updateOne(searchQuery, updateObject);
                } else {
                    // Crear documento
                    creados++;
                    Document document = new Document();
                    document.put("nombrepais", resultado.getString("NOM_PAIS"));
                    document.put("totalvendido", resultado.getInt("TOTAL_VENTAS"));
                    coleccion.insertOne(document);
                }

            }

            despues = antes + creados;
            noActualizados = antes - actualizados;
        }

        oracle.conn.close();

        return new ResultadosEstadisticas("Pais", antes, despues, actualizados, noActualizados, creados);
    }

    private ResultadosEstadisticas generarEtario() throws Exception {
        Oracle oracle = new Oracle();
        MongoDB mongoDb = new MongoDB();

        Statement sentencia = oracle.conn.createStatement();
        ResultSet resultado;

        resultado = sentencia.executeQuery(this.queryEtario);

        long antes = 0;
        long despues = 0;
        long actualizados = 0;
        long noActualizados = 0;
        long creados = 0;

        if (!mongoDb.conn.listCollectionNames().into(new ArrayList<>()).contains(this.coleccionEtario)) {
            mongoDb.conn.createCollection(this.coleccionEtario);
            MongoCollection<Document> coleccion = mongoDb.conn.getCollection(this.coleccionEtario);
            int contador = 0;
            while (resultado.next()) {
                contador++;
                Document document = new Document();
                document.put("nombrecategoria", resultado.getString("NOMBRE_CATEGORIA"));
                document.put("grupoetario", resultado.getString("GRUPO_ETARIO"));
                document.put("totalvendido", resultado.getInt("TOTAL_VENTAS"));
                coleccion.insertOne(document);
            }
            despues = contador;
            creados = contador;
        } else {
            MongoCollection<Document> coleccion = mongoDb.conn.getCollection(this.coleccionEtario);

            antes = coleccion.countDocuments();

            while (resultado.next()) {
                Document searchQuery = new Document();
                searchQuery.put("nombrecategoria", resultado.getString("NOMBRE_CATEGORIA"));
                searchQuery.put("grupoetario", resultado.getString("GRUPO_ETARIO"));
                long existeEstadistica = coleccion.countDocuments(searchQuery);
                if (existeEstadistica > 0) {
                    // Actualizar documento
                    actualizados++;
                    FindIterable<Document> cursor = coleccion.find(searchQuery);
                    Document document = new Document();
                    document.put("totalvendido", resultado.getInt("TOTAL_VENTAS") + cursor.first().getInteger("totalvendido"));
                    Document updateObject = new Document();
                    updateObject.put("$set", document);

                    coleccion.updateOne(searchQuery, updateObject);
                } else {
                    // Crear documento
                    creados++;
                    Document document = new Document();
                    document.put("nombrecategoria", resultado.getString("NOMBRE_CATEGORIA"));
                    document.put("grupoetario", resultado.getString("GRUPO_ETARIO"));
                    document.put("totalvendido", resultado.getInt("TOTAL_VENTAS"));
                    coleccion.insertOne(document);
                }

            }

            despues = antes + creados;
            noActualizados = antes - actualizados;
        }

        oracle.conn.close();

        return new ResultadosEstadisticas("Etario", antes, despues, actualizados, noActualizados, creados);
    }

    private void actualizarProcesados() throws Exception {
        Oracle oracle = new Oracle();
        MongoDB mongo = new MongoDB();

        Statement sentencia = oracle.conn.createStatement();
        sentencia.executeQuery(this.updateProcesados);

        oracle.conn.close();
    }
}
