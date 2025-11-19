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
 * Servlet para gestionar Libros con Autores
 */
@WebServlet(name = "SvLibros", urlPatterns = {"/SvLibros"})
public class SvLibros extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idBusqueda = request.getParameter("idBusqueda");
        String tituloBusqueda = request.getParameter("tituloBusqueda");
        JSONArray libros;

        // Prioridad: si hay búsqueda por título, usar esa; si no, buscar por ID; si no, listar todos
        if (tituloBusqueda != null && !tituloBusqueda.trim().isEmpty()) {
            libros = ApiCliente.buscarLibrosPorTitulo(tituloBusqueda);
        } else if (idBusqueda != null && !idBusqueda.trim().isEmpty()) {
            libros = ApiCliente.buscarLibrosPorId(idBusqueda);
        } else {
            libros = ApiCliente.getLibros();
        }

        // IMPORTANTE: Siempre cargamos autores para que el ComboBox funcione
        // incluso cuando estamos viendo resultados de búsqueda.
        JSONArray autores = ApiCliente.getAutores();

        request.setAttribute("libros", libros);
        request.setAttribute("autores", autores);

        // Para mantener el texto en los inputs de búsqueda
        request.setAttribute("idBusquedaActual", idBusqueda);
        request.setAttribute("tituloBusquedaActual", tituloBusqueda);

        request.getRequestDispatcher("/libros.jsp").forward(request, response);
    }

    // Crear Libro
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String titulo = request.getParameter("titulo");
        String idAutor = request.getParameter("autor"); // Viene del value del option
        String anio = request.getParameter("anio");

        boolean ok = ApiCliente.crearLibro(titulo, idAutor, anio);

        response.setStatus(ok ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
        // Redirigir para recargar la tabla
        response.sendRedirect(request.getContextPath() + "/SvLibros");
    }

    // Actualizar Libro
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        String titulo = request.getParameter("titulo");
        String idAutor = request.getParameter("autor");
        String anio = request.getParameter("anio");

        boolean ok = ApiCliente.actualizarLibro(id, titulo, idAutor, anio);

        response.setStatus(ok ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
    }

    // Eliminar Libro
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        boolean ok = ApiCliente.eliminarLibro(id);

        response.setStatus(ok ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
    }
}
