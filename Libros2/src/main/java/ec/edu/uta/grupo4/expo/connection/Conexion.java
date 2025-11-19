package ec.edu.uta.grupo4.expo.connection;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author USER
 */
public class Conexion {

    private static final String BASE_URL = "jdbc:mysql://localhost:3306/quintosoa1";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection obtenerConexion() {
        try {
            System.out.println("Intentando conectar a: " + BASE_URL);
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(BASE_URL, USER, PASSWORD);
            System.out.println("Conexión establecida correctamente");
            return conn;
        } catch (Exception e) {
            System.out.println("Error de conexión a la base de datos: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
