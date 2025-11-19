<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Validación de sesión/carga de datos
    if(request.getAttribute("autores") == null){
        response.sendRedirect(request.getContextPath() + "/SvAutores");
        return;
    }
    
    JSONArray autores = (JSONArray) request.getAttribute("autores");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Gestión de Autores</title>
        <script>
            // Eliminar Autor (DELETE)
            function eliminarAutor(id) {
                if (confirm("¿Eliminar autor con ID " + id + "?")) {
                    fetch("<%= request.getContextPath() %>/SvAutores?id=" + id, {
                        method: 'DELETE'
                    }).then(res => {
                        if (res.ok)
                            window.location.reload();
                        else
                            alert("Error al eliminar autor. Verifique que no tenga libros asociados.");
                    });
                }
            }

            // Cargar datos en formulario para Editar
            function cargarParaEditar(id, nombre, correo) {
                document.getElementById("form-titulo").innerText = "Editar Autor (ID: " + id + ")";
                document.getElementById("inputId").value = id;
                document.getElementById("inputNombre").value = nombre;
                document.getElementById("inputCorreo").value = correo;

                // Cambiar submit a PUT
                const form = document.getElementById("autorForm");
                form.onsubmit = function (event) {
                    event.preventDefault();
                    actualizarAutor();
                };
            }

            // Actualizar Autor (PUT)
            function actualizarAutor() {
                const data = new URLSearchParams(new FormData(document.getElementById("autorForm")));
                fetch("<%= request.getContextPath() %>/SvAutores?" + data.toString(), {
                    method: 'PUT'
                }).then(res => {
                    if (res.ok)
                        window.location.reload();
                    else
                        alert("Error al actualizar autor");
                });
            }

            // Limpiar formulario
            function limpiarFormulario() {
                document.getElementById("autorForm").reset();
                document.getElementById("form-titulo").innerText = "Nuevo Autor";
                document.getElementById("inputId").value = "";
                document.getElementById("autorForm").onsubmit = null; // Regresa al submit normal POST
            }
        </script>
    </head>
    <body>


        <h1>Gestión de Autores</h1>
        <a href="<%= request.getContextPath() %>/SvLibros">Ir a Libros</a>
        <div>
            <form method="GET" action="<%= request.getContextPath() %>/SvAutores">
                <label><strong>Buscar Autor:</strong></label>
                <br>
                <label>Por ID:</label>
                <input type="text" name="idBusqueda" placeholder="Ingrese ID..." 
                       value="<%= request.getAttribute("idBusquedaActual") != null ? request.getAttribute("idBusquedaActual") : "" %>">
                
                <label style="margin-left: 15px;">Por Nombre:</label>
                <input type="text" name="nombreBusqueda" placeholder="Ingrese nombre..." 
                       value="<%= request.getAttribute("nombreBusquedaActual") != null ? request.getAttribute("nombreBusquedaActual") : "" %>">

                <button type="submit">Buscar</button>

                <% if ((request.getAttribute("idBusquedaActual") != null && !request.getAttribute("idBusquedaActual").toString().isEmpty()) ||
                       (request.getAttribute("nombreBusquedaActual") != null && !request.getAttribute("nombreBusquedaActual").toString().isEmpty())) { %>
                <a href="<%= request.getContextPath() %>/SvAutores">
                    <button type="button">Ver Todos</button>
                </a>
                <% } %>
            </form>
        </div>

        <hr>

        <div>
            <div>
                <h3 id="form-titulo">Nuevo Autor</h3>
                <form id="autorForm" method="POST" action="<%= request.getContextPath() %>/SvAutores">
                    <input type="hidden" name="id" id="inputId">

                    <div>
                        <label>Nombre Completo:</label>
                        <input type="text" name="nombre" id="inputNombre" required>
                    </div>

                    <div>
                        <label>Correo Electrónico:</label>
                        <input type="email" name="correo" id="inputCorreo" required>
                    </div>
                    <br>
                    <button type="submit">Guardar</button>
                    <button type="button" onclick="limpiarFormulario()">Limpiar / Nuevo</button>
                </form>
            </div>

            <hr>

            <div>
                <h3>Listado de Autores</h3>
                <table border="1">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Correo</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                        if(autores != null && autores.length() > 0) {
                            for(int i=0; i<autores.length(); i++) {
                                JSONObject a = autores.getJSONObject(i);
                                String id = a.optString("ID_AUTOR");
                                String nombre = a.optString("NOMBRE_AUTOR");
                                String correo = a.optString("CORREO_AUTOR");
                        %>
                        <tr>
                            <td><%= id %></td>
                            <td><%= nombre %></td>
                            <td><%= correo %></td>
                            <td>
                                <button onclick="cargarParaEditar('<%= id %>', '<%= nombre %>', '<%= correo %>')">
                                    Editar
                                </button>
                                <button onclick="eliminarAutor('<%= id %>')">
                                    Eliminar
                                </button>
                            </td>
                        </tr>
                        <% 
                            }
                        } else { 
                        %>
                        <tr><td colspan="4">No hay autores registrados</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>

    </body>
</html>