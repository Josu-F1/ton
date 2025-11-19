/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package ec.edu.uta.grupo4.expo.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import ec.edu.uta.grupo4.expo.daos.EstudianteDAO;
import ec.edu.uta.grupo4.expo.models.Estudiante;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author USER
 */
@WebServlet(name = "SvEstudiante", urlPatterns = {"/SvEstudiante", "/svEstudiante", "/estudiantes", "/Estudiantes"})
public class SvEstudiante extends HttpServlet {

    private EstudianteDAO dao = new EstudianteDAO();
    Gson gson = new Gson();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SvEstudiante</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SvEstudiante at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String acceptHeader = request.getHeader("Accept");
        
        // Si la petición acepta JSON, responder con JSON
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            response.setContentType("application/json;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                List<Estudiante> lista = dao.listar();
                out.print(gson.toJson(lista));
            }
        } else {
            // Si es una petición desde el navegador, redirigir al JSP con los datos
            List<Estudiante> lista = dao.listar();
            JSONArray jsonArray = new JSONArray();
            
            for (Estudiante e : lista) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("cedula", e.getCedula());
                jsonObj.put("nombre", e.getNombre());
                jsonObj.put("apellido", e.getApellido());
                jsonObj.put("direccion", e.getDireccion());
                jsonObj.put("telefono", e.getTelefono());
                jsonArray.put(jsonObj);
            }
            
            request.setAttribute("estudiantes", jsonArray);
            request.getRequestDispatcher("/estudiantes.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String contentType = request.getContentType();
            
            if (contentType != null && contentType.contains("application/json")) {
                // Petición JSON
                BufferedReader reader = request.getReader();
                Estudiante e = gson.fromJson(reader, Estudiante.class);
                dao.agregar(e);
                
                response.setContentType("application/json");
                response.getWriter().print("{\"mensaje\":\"Estudiante agregado correctamente\"}");
            } else {
                // Petición de formulario HTML
                String cedula = request.getParameter("cedula");
                String nombre = request.getParameter("nombre");
                String apellido = request.getParameter("apellido");
                String direccion = request.getParameter("direccion");
                String telefono = request.getParameter("telefono");
                
                // Crear el estudiante
                Estudiante e = new Estudiante(cedula, nombre, apellido, direccion, telefono);
                dao.agregar(e);
                
                // Redirigir de vuelta a la lista
                response.sendRedirect(request.getContextPath() + "/SvEstudiante");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la solicitud: " + ex.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BufferedReader reader = request.getReader();
        Estudiante e = gson.fromJson(reader, Estudiante.class
        );
        boolean actualizado = dao.actualizar(e);

        response.setContentType("application/json");
        response.getWriter().print("{\"actualizado\": " + actualizado + "}");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String cedula = request.getParameter("cedula");
        boolean eliminado = dao.eliminar(cedula);

        response.setContentType("application/json");
        response.getWriter().print("{\"eliminado\": " + eliminado + "}");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
