package Conexiones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Oracle {
    private String user = "miguel";
    private String password = "123";
    private String url = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    public Connection conn;


    public Oracle() throws Exception {
        this.conexionOracle();
    }


    public void conexionOracle() throws Exception {

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (Exception e) {
            throw new Exception("No se pudo cargar el driver JDBC");
        }

        try {
            this.conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new Exception("No hay conexión con la base de datos Oracle.");
        }
    }
}
