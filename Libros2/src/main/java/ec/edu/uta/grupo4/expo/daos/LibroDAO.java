package ec.edu.uta.grupo4.expo.daos;

import ec.edu.uta.grupo4.expo.connection.Conexion;
import ec.edu.uta.grupo4.expo.models.Autor;
import ec.edu.uta.grupo4.expo.models.Libro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones CRUD de Libro
 */
public class LibroDAO {

    // Crear un nuevo libro
    public boolean crear(Libro libro) {
        String sql = "INSERT INTO libro (titulo, anio_publicacion, autor_id) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAnioPublicacion());
            stmt.setInt(3, libro.getAutor().getId());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        libro.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Error al crear libro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Leer todos los libros con información del autor
    public List<Libro> listar() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT l.*, a.nombre as autor_nombre, a.correo as autor_correo " +
                     "FROM libro l INNER JOIN autor a ON l.autor_id = a.id ORDER BY l.id";
        
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Autor autor = new Autor();
                autor.setId(rs.getInt("autor_id"));
                autor.setNombre(rs.getString("autor_nombre"));
                autor.setCorreo(rs.getString("autor_correo"));
                
                Libro libro = new Libro();
                libro.setId(rs.getInt("id"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAnioPublicacion(rs.getString("anio_publicacion"));
                libro.setAutor(autor);
                
                libros.add(libro);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar libros: " + e.getMessage());
            e.printStackTrace();
        }
        return libros;
    }

    // Buscar libro por ID
    public Libro buscarPorId(int id) {
        String sql = "SELECT l.*, a.nombre as autor_nombre, a.correo as autor_correo " +
                     "FROM libro l INNER JOIN autor a ON l.autor_id = a.id WHERE l.id = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Autor autor = new Autor();
                    autor.setId(rs.getInt("autor_id"));
                    autor.setNombre(rs.getString("autor_nombre"));
                    autor.setCorreo(rs.getString("autor_correo"));
                    
                    Libro libro = new Libro();
                    libro.setId(rs.getInt("id"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setAnioPublicacion(rs.getString("anio_publicacion"));
                    libro.setAutor(autor);
                    
                    return libro;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar libro por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Buscar libros por título (búsqueda parcial)
    public List<Libro> buscarPorTitulo(String titulo) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT l.*, a.nombre as autor_nombre, a.correo as autor_correo " +
                     "FROM libro l INNER JOIN autor a ON l.autor_id = a.id " +
                     "WHERE l.titulo LIKE ? ORDER BY l.id";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + titulo + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Autor autor = new Autor();
                    autor.setId(rs.getInt("autor_id"));
                    autor.setNombre(rs.getString("autor_nombre"));
                    autor.setCorreo(rs.getString("autor_correo"));
                    
                    Libro libro = new Libro();
                    libro.setId(rs.getInt("id"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setAnioPublicacion(rs.getString("anio_publicacion"));
                    libro.setAutor(autor);
                    
                    libros.add(libro);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar libro por título: " + e.getMessage());
            e.printStackTrace();
        }
        return libros;
    }

    // Listar libros por autor
    public List<Libro> listarPorAutor(int autorId) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT l.*, a.nombre as autor_nombre, a.correo as autor_correo " +
                     "FROM libro l INNER JOIN autor a ON l.autor_id = a.id " +
                     "WHERE l.autor_id = ? ORDER BY l.id";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, autorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Autor autor = new Autor();
                    autor.setId(rs.getInt("autor_id"));
                    autor.setNombre(rs.getString("autor_nombre"));
                    autor.setCorreo(rs.getString("autor_correo"));
                    
                    Libro libro = new Libro();
                    libro.setId(rs.getInt("id"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setAnioPublicacion(rs.getString("anio_publicacion"));
                    libro.setAutor(autor);
                    
                    libros.add(libro);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar libros por autor: " + e.getMessage());
            e.printStackTrace();
        }
        return libros;
    }

    // Actualizar libro
    public boolean actualizar(Libro libro) {
        String sql = "UPDATE libro SET titulo = ?, anio_publicacion = ?, autor_id = ? WHERE id = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAnioPublicacion());
            stmt.setInt(3, libro.getAutor().getId());
            stmt.setInt(4, libro.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar libro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar libro
    public boolean eliminar(int id) {
        String sql = "DELETE FROM libro WHERE id = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar libro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
