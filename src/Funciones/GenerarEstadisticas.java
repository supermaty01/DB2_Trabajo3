package Funciones;

import Conexiones.MongoDB;
import Conexiones.Oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GenerarEstadisticas {

    private String queryPais = 'SELECT CIUDAD.NOM_PAIS, SUM(VENTA.NUMERO_UNIDADES * VENTA.RECIO_UNITARIO) AS TOTAL_VENTAS FROM VENTA, JOIN SUCURSAL ON VENTA.COD_SUC = SUCURSAL.CODIGO_SUC, JOIN CIUDAD ON SUCURSAL.NOM_CIUDAD = CIUDAD.NOM_CIUDAD, TABLE(DETALLES) WHERE VENTA.ESTADO = "No Procesada" GROUP BY CIUDAD.NOM_PAIS';
    private String queryEtario = 'SELECT CATEGORIA.NOMBRE_CATEGORIA, CLIENTE.GRUPO_ETARIO, SUM(VENTA.NUMERO_UNIDADES * VENTA.RECIO_UNITARIO) AS TOTAL_VENTAS FROM VENTA, JOIN CLIENTE ON VENTA.PASAPORTE = CLIENTE.PASAPORTE, JOIN PRODUCTO ON VENTA.CODIGO_PRO = PRODUCTO.CODIGO_PRO, JOIN CATEGORIA ON PRODUCTO.CODIGO_CAT = CATEGORIA.CODIGO_CAT, TABLE(DETALLERS) WHERE VENTA.ESTADO = "No Procesada" GROUP BY CATEGORIA.NOMBRE_CATEGORIA, CLIENTE.GRUPO_ETARIO';
    private String updateProcesados = "UPDATE VENTA WHERE ESTADO = \"No Procesada\" SET ESTADO = \"Procesada\"";

    public void generarPais() {
        try {
            Oracle oracle = new Oracle();
            MongoDB mongo = new MongoDB();

            Statement sentencia = oracle.conn.createStatement();
            ResultSet resultado;

            resultado = sentencia.executeQuery(this.queryPais);

            if (!mongoDb.listCollectionNames().into(new ArrayList<>()).contains("totalventasporpais")) {
                mongoDb.createCollection("totalventasporpais");
            }

            MongoCollection<Document> coleccion = mongoDb.getCollection("totalventasporpais");

            //TODO: Agregar o editar documentos en la coleccion



            oracle.conn.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void generarEtario() {
        try {
            Oracle oracle = new Oracle();
            MongoDB mongo = new MongoDB();

            Statement sentencia = oracle.conn.createStatement();
            ResultSet resultado;

            resultado = sentencia.executeQuery(this.queryEtario);

            if (!mongoDb.listCollectionNames().into(new ArrayList<>()).contains("totalventasporpais")) {
                mongoDb.createCollection("totalventasporpais");
            }

            MongoCollection<Document> coleccion = mongoDb.getCollection("totalventasporpais");

            //TODO: Agregar o editar documentos en la coleccion



            oracle.conn.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
