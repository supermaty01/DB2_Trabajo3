import java.sql.*;

class conexion {

    static public void main(String[] args) {
        Connection conn;
        Statement sentencia;
        ResultSet resultado;
        System.out.println("Conexión a la base de datos...");
        try { // Se carga el driver JDBC-ODBC
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (Exception e) {
            System.out.println("No se pudo cargar el driver JDBC");
            return;
        }
        try { // Se establece la conexión con la base de datos
            conn = DriverManager.getConnection
                    ("jdbc:oracle:thin:@localhost:1521/XEPDB1", "MATEO", "123");
            sentencia = conn.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("No hay conexión con la base de datos.");
            return;
        }
        try {
            System.out.println("Seleccionando...");
            resultado = sentencia.executeQuery
                    ("SELECT codigo,nom,salario FROM empleado");
            //Se recorren las tuplas retornadas
            while (resultado.next()) {
                System.out.println(resultado.getInt("codigo") +
                        "---" + resultado.getString("nom") + "---" +
                        resultado.getInt("salario"));
            }
            conn.close(); //Cierre de la conexión
        } catch (SQLException e) {
            System.out.println("Error: " +
                    e.getMessage());
        }
        System.out.println("Consulta finalizada.");
    } //Fin del main
} //Fin de la clase