package Funciones;

import Conexiones.MongoDB;
import Conexiones.Oracle;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GenerarEstadisticas {

    private String queryPais = "SELECT CIUDAD.NOM_PAIS, SUM(VENTA.NUMERO_UNIDADES * VENTA.RECIO_UNITARIO) AS TOTAL_VENTAS FROM VENTA, JOIN SUCURSAL ON VENTA.COD_SUC = SUCURSAL.CODIGO_SUC, JOIN CIUDAD ON SUCURSAL.NOM_CIUDAD = CIUDAD.NOM_CIUDAD, TABLE(DETALLES) WHERE VENTA.ESTADO = \"No Procesada\" GROUP BY CIUDAD.NOM_PAIS";
    private String queryEtario = "SELECT CATEGORIA.NOMBRE_CATEGORIA, CLIENTE.GRUPO_ETARIO, SUM(VENTA.NUMERO_UNIDADES * VENTA.RECIO_UNITARIO) AS TOTAL_VENTAS FROM VENTA, JOIN CLIENTE ON VENTA.PASAPORTE = CLIENTE.PASAPORTE, JOIN PRODUCTO ON VENTA.CODIGO_PRO = PRODUCTO.CODIGO_PRO, JOIN CATEGORIA ON PRODUCTO.CODIGO_CAT = CATEGORIA.CODIGO_CAT, TABLE(DETALLERS) WHERE VENTA.ESTADO = \"No Procesada\" GROUP BY CATEGORIA.NOMBRE_CATEGORIA, CLIENTE.GRUPO_ETARIO";
    private String updateProcesados = "UPDATE VENTA WHERE ESTADO = \"No Procesada\" SET ESTADO = \"Procesada\"";

    private String coleccionPais = "totalventasporpais";
    private String coleccionEtario = "totalventasporcatygedad";


    public void generarEstadisticas() {
        try {
            generarPais();
            generarEtario();
            actualizarProcesados();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void generarPais() throws Exception {
        Oracle oracle = new Oracle();
        MongoDB mongo = new MongoDB();

        Statement sentencia = oracle.conn.createStatement();
        ResultSet resultado = sentencia.executeQuery(this.queryPais);

        if (!mongoDb.listCollectionNames().into(new ArrayList<>()).contains(this.coleccionPais)) {
            mongoDb.createCollection(this.coleccionPais);
        }

        MongoCollection<Document> coleccion = mongoDb.getCollection(this.coleccionPais);

        //TODO: Agregar o editar documentos en la coleccion


        oracle.conn.close();
    }

    private void generarEtario() throws Exception {
        Oracle oracle = new Oracle();
        MongoDB mongo = new MongoDB();

        Statement sentencia = oracle.conn.createStatement();
        ResultSet resultado;

        resultado = sentencia.executeQuery(this.queryEtario);

        if (!mongoDb.listCollectionNames().into(new ArrayList<>()).contains(this.coleccionEtario)) {
            mongoDb.createCollection(this.coleccionEtario);
        }

        MongoCollection<Document> coleccion = mongoDb.getCollection(this.coleccionEtario);

        //TODO: Agregar o editar documentos en la coleccion


        oracle.conn.close();
    }

    private void actualizarProcesados() throws Exception {
        Oracle oracle = new Oracle();
        MongoDB mongo = new MongoDB();

        Statement sentencia = oracle.conn.createStatement();
        sentencia.executeQuery(this.updateProcesados);

        oracle.conn.close();
    }
}
