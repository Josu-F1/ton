package ec.edu.uta.grupo4.expo.servlet;

import ec.edu.uta.grupo4.expo.daos.AutorDAO;
import ec.edu.uta.grupo4.expo.models.Autor;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet para gestionar operaciones CRUD de Autor
 */
@WebServlet(name = "SvAutor", urlPatterns = {"/api/autor"})
public class SvAutor extends HttpServlet {

    private final AutorDAO autorDAO = new AutorDAO();
    private final Gson gson = new Gson();

    // GET - Listar todos, buscar por ID o por nombre
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String idParam = request.getParameter("id");
        String nombreParam = request.getParameter("nombre");
        
        try {
            if (idParam != null && !idParam.isEmpty()) {
                // Buscar por ID
                int id = Integer.parseInt(idParam);
                Autor autor = autorDAO.buscarPorId(id);
                if (autor != null) {
                    out.print(gson.toJson(autor));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Autor no encontrado\"}");
                }
            } else if (nombreParam != null && !nombreParam.isEmpty()) {
                // Buscar por nombre
                List<Autor> autores = autorDAO.buscarPorNombre(nombreParam);
                out.print(gson.toJson(autores));
            } else {
                // Listar todos
                List<Autor> autores = autorDAO.listar();
                out.print(gson.toJson(autores));
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"ID inválido\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error del servidor: " + e.getMessage() + "\"}");
        }
    }

    // POST - Crear nuevo autor
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            Autor autor = gson.fromJson(request.getReader(), Autor.class);
            
            if (autor.getNombre() == null || autor.getNombre().trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"El nombre es obligatorio\"}");
                return;
            }
            
            if (autor.getCorreo() == null || autor.getCorreo().trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"El correo es obligatorio\"}");
                return;
            }
            
            boolean creado = autorDAO.crear(autor);
            if (creado) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print(gson.toJson(autor));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"No se pudo crear el autor\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error del servidor: " + e.getMessage() + "\"}");
        }
    }

    // PUT - Actualizar autor
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            Autor autor = gson.fromJson(request.getReader(), Autor.class);
            
            if (autor.getId() <= 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"ID inválido\"}");
                return;
            }
            
            if (autor.getNombre() == null || autor.getNombre().trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"El nombre es obligatorio\"}");
                return;
            }
            
            if (autor.getCorreo() == null || autor.getCorreo().trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"El correo es obligatorio\"}");
                return;
            }
            
            boolean actualizado = autorDAO.actualizar(autor);
            if (actualizado) {
                out.print(gson.toJson(autor));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Autor no encontrado\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error del servidor: " + e.getMessage() + "\"}");
        }
    }

    // DELETE - Eliminar autor
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
            boolean eliminado = autorDAO.eliminar(id);
            
            if (eliminado) {
                out.print("{\"mensaje\": \"Autor eliminado exitosamente\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Autor no encontrado\"}");
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
