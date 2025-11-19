package ec.edu.uta.grupo4.expo.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ec.edu.uta.grupo4.expo.connection.Conexion;
import ec.edu.uta.grupo4.expo.models.Estudiante;

/**
 *
 * @author USER
 */
public class EstudianteDAO {

    public List<Estudiante> listar() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT * FROM estudiantes";

        try (Connection conn = Conexion.obtenerConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Estudiante(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("direccion"),
                        rs.getString("telefono")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }


    public Estudiante agregar(Estudiante e) {
        String sql = "INSERT INTO estudiantes (cedula, nombre, apellido, direccion, telefono) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = Conexion.obtenerConexion();
            if (conn == null) {
                System.out.println("Error: No se pudo obtener conexión a la base de datos");
                return null;
            }
            
            System.out.println("Conexión obtenida correctamente");
            System.out.println("Ejecutando SQL: " + sql);
            System.out.println("Datos: " + e.getCedula() + ", " + e.getNombre() + ", " + e.getApellido() + ", " + e.getDireccion() + ", " + e.getTelefono());
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, e.getCedula());
                ps.setString(2, e.getNombre());
                ps.setString(3, e.getApellido());
                ps.setString(4, e.getDireccion());
                ps.setString(5, e.getTelefono());
                
                int rowsAffected = ps.executeUpdate();
                System.out.println("Filas afectadas: " + rowsAffected);
                
                if (rowsAffected > 0) {
                    System.out.println("Estudiante insertado correctamente");
                    return e;
                } else {
                    System.out.println("No se insertó ninguna fila");
                    return null;
                }
            }
        } catch (Exception ex) {
            System.out.println("Error en agregar estudiante: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ex) {
                    System.out.println("Error cerrando conexión: " + ex.getMessage());
                }
            }
        }
    }

    public boolean actualizar(Estudiante e) {
        String sql = "UPDATE estudiantes SET nombre = ?, apellido = ?, direccion = ?, telefono = ? WHERE cedula = ?";
        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellido());
            ps.setString(3, e.getDireccion());
            ps.setString(4, e.getTelefono());
            ps.setString(5, e.getCedula());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(String cedula) {
        String sql = "DELETE FROM estudiantes WHERE cedula = ?";
        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
