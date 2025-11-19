package ec.edu.uta.grupo4.expo.daos;

import ec.edu.uta.grupo4.expo.connection.Conexion;
import ec.edu.uta.grupo4.expo.models.Autor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones CRUD de Autor
 */
public class AutorDAO {

    // Crear un nuevo autor
    public boolean crear(Autor autor) {
        String sql = "INSERT INTO autor (nombre, correo) VALUES (?, ?)";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, autor.getNombre());
            stmt.setString(2, autor.getCorreo());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        autor.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Error al crear autor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Leer todos los autores
    public List<Autor> listar() {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT * FROM autor ORDER BY id";
        
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Autor autor = new Autor();
                autor.setId(rs.getInt("id"));
                autor.setNombre(rs.getString("nombre"));
                autor.setCorreo(rs.getString("correo"));
                autores.add(autor);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar autores: " + e.getMessage());
            e.printStackTrace();
        }
        return autores;
    }

    // Buscar autor por ID
    public Autor buscarPorId(int id) {
        String sql = "SELECT * FROM autor WHERE id = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Autor autor = new Autor();
                    autor.setId(rs.getInt("id"));
                    autor.setNombre(rs.getString("nombre"));
                    autor.setCorreo(rs.getString("correo"));
                    return autor;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar autor por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Buscar autores por nombre (búsqueda parcial)
    public List<Autor> buscarPorNombre(String nombre) {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT * FROM autor WHERE nombre LIKE ? ORDER BY id";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nombre + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Autor autor = new Autor();
                    autor.setId(rs.getInt("id"));
                    autor.setNombre(rs.getString("nombre"));
                    autor.setCorreo(rs.getString("correo"));
                    autores.add(autor);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar autor por nombre: " + e.getMessage());
            e.printStackTrace();
        }
        return autores;
    }

    // Actualizar autor
    public boolean actualizar(Autor autor) {
        String sql = "UPDATE autor SET nombre = ?, correo = ? WHERE id = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, autor.getNombre());
            stmt.setString(2, autor.getCorreo());
            stmt.setInt(3, autor.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar autor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar autor
    public boolean eliminar(int id) {
        String sql = "DELETE FROM autor WHERE id = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar autor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
