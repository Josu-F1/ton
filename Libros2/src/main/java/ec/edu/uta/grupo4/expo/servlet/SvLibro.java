package ec.edu.uta.grupo4.expo.servlet;

import ec.edu.uta.grupo4.expo.daos.AutorDAO;
import ec.edu.uta.grupo4.expo.daos.LibroDAO;
import ec.edu.uta.grupo4.expo.models.Autor;
import ec.edu.uta.grupo4.expo.models.Libro;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet para gestionar operaciones CRUD de Libro
 */
@WebServlet(name = "SvLibro", urlPatterns = {"/api/libro"})
public class SvLibro extends HttpServlet {

    private final LibroDAO libroDAO = new LibroDAO();
    private final AutorDAO autorDAO = new AutorDAO();
    private final Gson gson = new Gson();

    // GET - Listar todos, buscar por ID o por título
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String idParam = request.getParameter("id");
        String tituloParam = request.getParameter("titulo");
        String autorIdParam = request.getParameter("autorId");
        
        try {
            if (idParam != null && !idParam.isEmpty()) {
                // Buscar por ID
                int id = Integer.parseInt(idParam);
                Libro libro = libroDAO.buscarPorId(id);
                if (libro != null) {
                    out.print(gson.toJson(libro));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Libro no encontrado\"}");
                }
            } else if (tituloParam != null && !tituloParam.isEmpty()) {
                // Buscar por título
                List<Libro> libros = libroDAO.buscarPorTitulo(tituloParam);
                out.print(gson.toJson(libros));
            } else if (autorIdParam != null && !autorIdParam.isEmpty()) {
                // Listar por autor
                int autorId = Integer.parseInt(autorIdParam);
                List<Libro> libros = libroDAO.listarPorAutor(autorId);
                out.print(gson.toJson(libros));
            } else {
                // Listar todos
                List<Libro> libros = libroDAO.listar();
                out.print(gson.toJson(libros));
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"ID inválido\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error del servidor: " + e.getMessage() + "\"}");
        }
    }

    // POST - Crear nuevo libro
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);
            
            String titulo = jsonObject.get("titulo").getAsString();
            String anioPublicacion = jsonObject.get("anioPublicacion").getAsString();
            int autorId = jsonObject.get("autorId").getAsInt();
            
            if (titulo == null || titulo.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"El título es obligatorio\"}");
                return;
            }
            
            if (anioPublicacion == null || anioPublicacion.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"El año de publicación es obligatorio\"}");
                return;
            }
            
            Autor autor = autorDAO.buscarPorId(autorId);
            if (autor == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"El autor especificado no existe\"}");
                return;
            }
            
            Libro libro = new Libro(titulo, anioPublicacion, autor);
            boolean creado = libroDAO.crear(libro);
            
            if (creado) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print(gson.toJson(libro));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"No se pudo crear el libro\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error del servidor: " + e.getMessage() + "\"}");
        }
    }

    // PUT - Actualizar libro
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);
            
            int id = jsonObject.get("id").getAsInt();
            String titulo = jsonObject.get("titulo").getAsString();
            String anioPublicacion = jsonObject.get("anioPublicacion").getAsString();
            int autorId = jsonObject.get("autorId").getAsInt();
            
            if (id <= 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"ID inválido\"}");
                return;
            }
            
            if (titulo == null || titulo.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"El título es obligatorio\"}");
                return;
            }
            
            if (anioPublicacion == null || anioPublicacion.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"El año de publicación es obligatorio\"}");
                return;
            }
            
            Autor autor = autorDAO.buscarPorId(autorId);
            if (autor == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"El autor especificado no existe\"}");
                return;
            }
            
            Libro libro = new Libro(titulo, anioPublicacion, autor);
            libro.setId(id);
            
            boolean actualizado = libroDAO.actualizar(libro);
            if (actualizado) {
                out.print(gson.toJson(libro));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Libro no encontrado\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error del servidor: " + e.getMessage() + "\"}");
        }
    }

    // DELETE - Eliminar libro
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String idParam = request.getParameter("id");
        
        try {
            if (idParam == null || idParam.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Se requiere el parámetro id\"}");
                return;
            }
            
            int id = Integer.parseInt(idParam);
            boolean eliminado = libroDAO.eliminar(id);
            
            if (eliminado) {
                out.print("{\"mensaje\": \"Libro eliminado exitosamente\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Libro no encontrado\"}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"ID inválido\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error del servidor: " + e.getMessage() + "\"}");
        }
    }
}
