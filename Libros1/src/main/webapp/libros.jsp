<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Si entran directo al JSP sin pasar por el Servlet, redirigir
    if(request.getAttribute("libros") == null || request.getAttribute("autores") == null){
        response.sendRedirect(request.getContextPath() + "/SvLibros");
        return;
    }
    
    JSONArray libros = (JSONArray) request.getAttribute("libros");
    JSONArray autores = (JSONArray) request.getAttribute("autores");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Gestión de Libros</title>
        <script>
            // Función para Eliminar (DELETE)
            function eliminarLibro(id) {
                if (confirm("¿Eliminar libro con ID " + id + "?")) {
                    fetch("<%= request.getContextPath() %>/SvLibros?id=" + id, {
                        method: 'DELETE'
                    }).then(res => {
                        if (res.ok)
                            window.location.reload();
                        else
                            alert("Error al eliminar");
                    });
                }
            }

            // Función para preparar el formulario para Editar
            function cargarParaEditar(id, titulo, idAutor, anio) {
                document.getElementById("form-titulo").innerText = "Editar Libro (ID: " + id + ")";
                document.getElementById("inputId").value = id;
                document.getElementById("inputTitulo").value = titulo;
                document.getElementById("inputAnio").value = anio;
                document.getElementById("selectAutor").value = idAutor; // Selecciona el autor correcto en el combo

                // Cambiar el comportamiento del botón para hacer PUT en lugar de POST
                const form = document.getElementById("libroForm");
                form.onsubmit = function (event) {
                    event.preventDefault();
                    actualizarLibro();
                };
            }

            // Función para Actualizar (PUT)
            function actualizarLibro() {
                const data = new URLSearchParams(new FormData(document.getElementById("libroForm")));
                fetch("<%= request.getContextPath() %>/SvLibros?" + data.toString(), {
                    method: 'PUT'
                }).then(res => {
                    if (res.ok)
                        window.location.reload();
                    else
                        alert("Error al actualizar");
                });
            }

            function limpiarFormulario() {
                document.getElementById("libroForm").reset();
                document.getElementById("form-titulo").innerText = "Nuevo Libro";
                document.getElementById("inputId").value = "";
                // Restaurar el submit normal (POST)
                document.getElementById("libroForm").onsubmit = null;
            }
        </script>
    </head>
    <body>

        <h1>Gestión de Libros y Autores</h1>
        <a href="<%= request.getContextPath() %>/SvAutores">Ir a Autores</a>
        
        <div>
            <form method="GET" action="<%= request.getContextPath() %>/SvLibros">
                <label><strong>Buscar Libro:</strong></label>
                <br>
                <label>Por ID:</label>
                <input type="text" name="idBusqueda" placeholder="Ingrese ID..." 
                       value="<%= request.getAttribute("idBusquedaActual") != null ? request.getAttribute("idBusquedaActual") : "" %>">
                
                <label style="margin-left: 15px;">Por Título:</label>
                <input type="text" name="tituloBusqueda" placeholder="Ingrese título..." 
                       value="<%= request.getAttribute("tituloBusquedaActual") != null ? request.getAttribute("tituloBusquedaActual") : "" %>">

                <button type="submit">Buscar</button>

                <% if ((request.getAttribute("idBusquedaActual") != null && !request.getAttribute("idBusquedaActual").toString().isEmpty()) ||
                       (request.getAttribute("tituloBusquedaActual") != null && !request.getAttribute("tituloBusquedaActual").toString().isEmpty())) { %>
                <a href="<%= request.getContextPath() %>/SvLibros">
                    <button type="button">Ver Todos</button>
                </a>
                <% } %>
            </form>
        </div>
        
        <hr>

        <div>
            <div>
                <h3 id="form-titulo">Nuevo Libro</h3>
                <form id="libroForm" method="POST" action="<%= request.getContextPath() %>/SvLibros">
                    <input type="hidden" name="id" id="inputId">

                    <div>
                        <label>Título:</label>
                        <input type="text" name="titulo" id="inputTitulo" required>
                    </div>

                    <div>
                        <label>Autor:</label>
                        <select name="autor" id="selectAutor" required>
                            <option value="">-- Seleccione un Autor --</option>
                            <% 
                            if(autores != null) {
                                for(int i=0; i<autores.length(); i++) {
                                    JSONObject a = autores.getJSONObject(i);
                            %>
                            <option value="<%= a.optString("ID_AUTOR") %>">
                                <%= a.optString("NOMBRE_AUTOR") %>
                            </option>
                            <% 
                                }
                            } 
                            %>
                        </select>
                    </div>

                    <div>
                        <label>Año:</label>
                        <input type="number" name="anio" id="inputAnio" required>
                    </div>
                    <br>
                    <button type="submit">Guardar</button>
                    <button type="button" onclick="limpiarFormulario()">Limpiar / Nuevo</button>
                </form>
            </div>

            <hr>

            <div>
                <h3>Listado de Libros</h3>
                <table border="1">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Título</th>
                            <th>Autor (Nombre)</th>
                            <th>Año</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                        if(libros != null && libros.length() > 0) {
                            for(int i=0; i<libros.length(); i++) {
                                JSONObject l = libros.getJSONObject(i);
                                
                                String idLibro = l.optString("ID_LIBRO");
                                String titulo = l.optString("TITULO_LIBRO");
                                String nombreAutor = l.optString("AUTOR"); 
                                String idAutor = l.optString("AUTOR_ID_PER"); 
                                String anio = l.optString("ANIO_LIBRO");
                        %>
                        <tr>
                            <td><%= idLibro %></td>
                            <td><%= titulo %></td>
                            <td><%= nombreAutor %></td>
                            <td><%= anio %></td>
                            <td>
                                <button onclick="cargarParaEditar('<%= idLibro %>', '<%= titulo %>', '<%= idAutor %>', '<%= anio %>')">
                                    Editar
                                </button>
                                <button onclick="eliminarLibro('<%= idLibro %>')">
                                    Eliminar
                                </button>
                            </td>
                        </tr>
                        <% 
                            }
                        } else { 
                        %>
                        <tr><td colspan="5">No hay libros registrados</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>

    </body>
</html>