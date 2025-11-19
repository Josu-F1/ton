package ec.edu.uta.grupo4.librosjavawebver2.servlets;

import java.io.IOException;

import org.json.JSONArray;

import ec.edu.uta.grupo4.librosjavawebver2.util.ApiCliente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet para gestión CRUD de Autores
 */
@WebServlet(name = "SvAutores", urlPatterns = {"/SvAutores"})
public class SvAutores extends HttpServlet {

    // Listar Autores
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idBusqueda = request.getParameter("idBusqueda");
        String nombreBusqueda = request.getParameter("nombreBusqueda");
        JSONArray autores;

        // Prioridad: si hay búsqueda por nombre, usar esa; si no, buscar por ID; si no, listar todos
        if (nombreBusqueda != null && !nombreBusqueda.trim().isEmpty()) {
            autores = ApiCliente.buscarAutorPorNombre(nombreBusqueda);
        } else if (idBusqueda != null && !idBusqueda.trim().isEmpty()) {
            autores = ApiCliente.buscarAutorPorId(idBusqueda);
        } else {
            autores = ApiCliente.getAutores();
        }

        request.setAttribute("autores", autores);
        request.setAttribute("idBusquedaActual", idBusqueda); // Para mantener el valor en el input
        request.setAttribute("nombreBusquedaActual", nombreBusqueda); // Para mantener el valor en el input

        request.getRequestDispatcher("/autores.jsp").forward(request, response);
    }

    // Crear Autor
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");

        boolean ok = ApiCliente.crearAutor(nombre, correo);

        response.setStatus(ok ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
        response.sendRedirect(request.getContextPath() + "/SvAutores");
    }

    // Actualizar Autor
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");

        boolean ok = ApiCliente.actualizarAutor(id, nombre, correo);

        response.setStatus(ok ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
    }

    // Eliminar Autor
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        boolean ok = ApiCliente.eliminarAutor(id);

        response.setStatus(ok ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
    }
}
