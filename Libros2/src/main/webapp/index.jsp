<%@page import="ec.edu.uta.grupo4.expo.models.Libro"%>
<%@page import="ec.edu.uta.grupo4.expo.models.Autor"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ec.edu.uta.grupo4.expo.daos.AutorDAO"%>
<%@page import="ec.edu.uta.grupo4.expo.daos.LibroDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sistema de Autores y Libros</title>
</head>
<body>
    <h1>Sistema de Gestion de Autores y Libros</h1>
    
    <%
        AutorDAO autorDAO = new AutorDAO();
        LibroDAO libroDAO = new LibroDAO();
        
        // Procesamiento de acciones CRUD para Autores
        String accionAutor = request.getParameter("accionAutor");
        if (accionAutor != null) {
            if ("crear".equals(accionAutor)) {
                String nombre = request.getParameter("nombreAutor");
                String correo = request.getParameter("correoAutor");
                if (nombre != null && correo != null && !nombre.trim().isEmpty() && !correo.trim().isEmpty()) {
                    Autor nuevoAutor = new Autor(nombre, correo);
                    autorDAO.crear(nuevoAutor);
                }
            } else if ("editar".equals(accionAutor)) {
                String idStr = request.getParameter("idAutor");
                String nombre = request.getParameter("nombreAutor");
                String correo = request.getParameter("correoAutor");
                if (idStr != null && nombre != null && correo != null) {
                    try {
                        int id = Integer.parseInt(idStr);
                        Autor autor = new Autor(nombre, correo);
                        autor.setId(id);
                        autorDAO.actualizar(autor);
                    } catch (NumberFormatException e) {}
                }
            } else if ("eliminar".equals(accionAutor)) {
                String idStr = request.getParameter("idAutor");
                if (idStr != null) {
                    try {
                        int id = Integer.parseInt(idStr);
                        autorDAO.eliminar(id);
                    } catch (NumberFormatException e) {}
                }
            }
            response.sendRedirect("index.jsp");
            return;
        }
        
        // Procesamiento de acciones CRUD para Libros
        String accionLibro = request.getParameter("accionLibro");
        if (accionLibro != null) {
            if ("crear".equals(accionLibro)) {
                String titulo = request.getParameter("tituloLibro");
                String anio = request.getParameter("anioLibro");
                String autorIdStr = request.getParameter("autorIdLibro");
                if (titulo != null && anio != null && autorIdStr != null && !titulo.trim().isEmpty()) {
                    try {
                        int autorId = Integer.parseInt(autorIdStr);
                        Autor autor = autorDAO.buscarPorId(autorId);
                        if (autor != null) {
                            Libro nuevoLibro = new Libro(titulo, anio, autor);
                            libroDAO.crear(nuevoLibro);
                        }
                    } catch (NumberFormatException e) {}
                }
            } else if ("editar".equals(accionLibro)) {
                String idStr = request.getParameter("idLibro");
                String titulo = request.getParameter("tituloLibro");
                String anio = request.getParameter("anioLibro");
                String autorIdStr = request.getParameter("autorIdLibro");
                if (idStr != null && titulo != null && anio != null && autorIdStr != null) {
                    try {
                        int id = Integer.parseInt(idStr);
                        int autorId = Integer.parseInt(autorIdStr);
                        Autor autor = autorDAO.buscarPorId(autorId);
                        if (autor != null) {
                            Libro libro = new Libro(titulo, anio, autor);
                            libro.setId(id);
                            libroDAO.actualizar(libro);
                        }
                    } catch (NumberFormatException e) {}
                }
            } else if ("eliminar".equals(accionLibro)) {
                String idStr = request.getParameter("idLibro");
                if (idStr != null) {
                    try {
                        int id = Integer.parseInt(idStr);
                        libroDAO.eliminar(id);
                    } catch (NumberFormatException e) {}
                }
            }
            response.sendRedirect("index.jsp");
            return;
        }
        
        // Búsqueda de autores
        List<Autor> autores = null;
        String buscarAutorId = request.getParameter("buscarAutorId");
        String buscarAutorNombre = request.getParameter("buscarAutorNombre");
        String mensajeAutor = "";
        
        if (buscarAutorId != null && !buscarAutorId.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(buscarAutorId);
                Autor autor = autorDAO.buscarPorId(id);
                autores = new ArrayList<>();
                if (autor != null) {
                    autores.add(autor);
                } else {
                    mensajeAutor = "No se encontró autor con ID: " + id;
                }
            } catch (NumberFormatException e) {
                mensajeAutor = "ID inválido";
            }
        } else if (buscarAutorNombre != null && !buscarAutorNombre.trim().isEmpty()) {
            autores = autorDAO.buscarPorNombre(buscarAutorNombre);
            if (autores.isEmpty()) {
                mensajeAutor = "No se encontraron autores con el nombre: " + buscarAutorNombre;
            }
        } else {
            autores = autorDAO.listar();
        }
        
        // Búsqueda de libros
        List<Libro> libros = null;
        String buscarLibroId = request.getParameter("buscarLibroId");
        String buscarLibroTitulo = request.getParameter("buscarLibroTitulo");
        String mensajeLibro = "";
        
        if (buscarLibroId != null && !buscarLibroId.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(buscarLibroId);
                Libro libro = libroDAO.buscarPorId(id);
                libros = new ArrayList<>();
                if (libro != null) {
                    libros.add(libro);
                } else {
                    mensajeLibro = "No se encontró libro con ID: " + id;
                }
            } catch (NumberFormatException e) {
                mensajeLibro = "ID inválido";
            }
        } else if (buscarLibroTitulo != null && !buscarLibroTitulo.trim().isEmpty()) {
            libros = libroDAO.buscarPorTitulo(buscarLibroTitulo);
            if (libros.isEmpty()) {
                mensajeLibro = "No se encontraron libros con el título: " + buscarLibroTitulo;
            }
        } else {
            libros = libroDAO.listar();
        }
    %>
    
    <h2>Buscar Autores</h2>
    <form method="get">
        <label>Buscar por ID:</label>
        <input type="number" name="buscarAutorId" placeholder="Ingrese ID">
        <button type="submit">Buscar</button>
    </form>
    <br>
    <form method="get">
        <label>Buscar por Nombre:</label>
        <input type="text" name="buscarAutorNombre" placeholder="Ingrese nombre">
        <button type="submit">Buscar</button>
    </form>
    <br>
    <a href="index.jsp"><button type="button">Refrescar / Ver Todos</button></a>
    
    <% if (!mensajeAutor.isEmpty()) { %>
        <p style="color: red;"><strong><%= mensajeAutor %></strong></p>
    <% } %>
    
    <h2>Crear Autor</h2>
    <form method="post">
        <input type="hidden" name="accionAutor" value="crear">
        <label>Nombre:</label>
        <input type="text" name="nombreAutor" required>
        <label>Correo:</label>
        <input type="email" name="correoAutor" required>
        <button type="submit">Crear Autor</button>
    </form>
    
    <%
        String editarAutorId = request.getParameter("editarAutorId");
        if (editarAutorId != null && !editarAutorId.isEmpty()) {
            try {
                int id = Integer.parseInt(editarAutorId);
                Autor autorEditar = autorDAO.buscarPorId(id);
                if (autorEditar != null) {
    %>
    <h2>Editar Autor</h2>
    <form method="post">
        <input type="hidden" name="accionAutor" value="editar">
        <input type="hidden" name="idAutor" value="<%= autorEditar.getId() %>">
        <label>Nombre:</label>
        <input type="text" name="nombreAutor" value="<%= autorEditar.getNombre() %>" required>
        <label>Correo:</label>
        <input type="email" name="correoAutor" value="<%= autorEditar.getCorreo() %>" required>
        <button type="submit">Actualizar Autor</button>
        <a href="index.jsp"><button type="button">Cancelar</button></a>
    </form>
    <%
                }
            } catch (NumberFormatException e) {}
        }
    %>
    
    <h2>Lista de Autores</h2>
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Correo</th>
            <th>Acciones</th>
        </tr>
        <%
            if (autores != null && !autores.isEmpty()) {
                for (Autor autor : autores) {
        %>
        <tr>
            <td><%= autor.getId() %></td>
            <td><%= autor.getNombre() %></td>
            <td><%= autor.getCorreo() %></td>
            <td>
                <a href="index.jsp?editarAutorId=<%= autor.getId() %>"><button type="button">Editar</button></a>
                <form method="post" style="display:inline;" onsubmit="return confirm('¿Eliminar este autor?');">
                    <input type="hidden" name="accionAutor" value="eliminar">
                    <input type="hidden" name="idAutor" value="<%= autor.getId() %>">
                    <button type="submit">Eliminar</button>
                </form>
            </td>
        </tr>
        <%
                }
            } else {
        %>
        <tr>
            <td colspan="4">No hay autores registrados</td>
        </tr>
        <%
            }
        %>
    </table>
    
    <hr>
    
    <h2>Buscar Libros</h2>
    <form method="get">
        <label>Buscar por ID:</label>
        <input type="number" name="buscarLibroId" placeholder="Ingrese ID">
        <button type="submit">Buscar</button>
    </form>
    <br>
    <form method="get">
        <label>Buscar por Título:</label>
        <input type="text" name="buscarLibroTitulo" placeholder="Ingrese título">
        <button type="submit">Buscar</button>
    </form>
    <br>
    <a href="index.jsp"><button type="button">Refrescar / Ver Todos</button></a>
    
    <% if (!mensajeLibro.isEmpty()) { %>
        <p style="color: red;"><strong><%= mensajeLibro %></strong></p>
    <% } %>
    
    <h2>Crear Libro</h2>
    <form method="post">
        <input type="hidden" name="accionLibro" value="crear">
        <label>Título:</label>
        <input type="text" name="tituloLibro" required>
        <label>Año:</label>
        <input type="text" name="anioLibro" maxlength="4" required>
        <label>Autor:</label>
        <select name="autorIdLibro" required>
            <option value="">Seleccione un autor</option>
            <%
                List<Autor> todosAutores = autorDAO.listar();
                for (Autor a : todosAutores) {
            %>
            <option value="<%= a.getId() %>"><%= a.getNombre() %></option>
            <% } %>
        </select>
        <button type="submit">Crear Libro</button>
    </form>
    
    <%
        String editarLibroId = request.getParameter("editarLibroId");
        if (editarLibroId != null && !editarLibroId.isEmpty()) {
            try {
                int id = Integer.parseInt(editarLibroId);
                Libro libroEditar = libroDAO.buscarPorId(id);
                if (libroEditar != null) {
    %>
    <h2>Editar Libro</h2>
    <form method="post">
        <input type="hidden" name="accionLibro" value="editar">
        <input type="hidden" name="idLibro" value="<%= libroEditar.getId() %>">
        <label>Título:</label>
        <input type="text" name="tituloLibro" value="<%= libroEditar.getTitulo() %>" required>
        <label>Año:</label>
        <input type="text" name="anioLibro" value="<%= libroEditar.getAnioPublicacion() %>" maxlength="4" required>
        <label>Autor:</label>
        <select name="autorIdLibro" required>
            <%
                List<Autor> todosAutoresEdit = autorDAO.listar();
                for (Autor a : todosAutoresEdit) {
            %>
            <option value="<%= a.getId() %>" <%= a.getId() == libroEditar.getAutor().getId() ? "selected" : "" %>><%= a.getNombre() %></option>
            <% } %>
        </select>
        <button type="submit">Actualizar Libro</button>
        <a href="index.jsp"><button type="button">Cancelar</button></a>
    </form>
    <%
                }
            } catch (NumberFormatException e) {}
        }
    %>
    
    <h2>Lista de Libros</h2>
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Titulo</th>
            <th>Año</th>
            <th>Autor</th>
            <th>Acciones</th>
        </tr>
        <%
            if (libros != null && !libros.isEmpty()) {
                for (Libro libro : libros) {
        %>
        <tr>
            <td><%= libro.getId() %></td>
            <td><%= libro.getTitulo() %></td>
            <td><%= libro.getAnioPublicacion() %></td>
            <td><%= libro.getAutor().getNombre() %></td>
            <td>
                <a href="index.jsp?editarLibroId=<%= libro.getId() %>"><button type="button">Editar</button></a>
                <form method="post" style="display:inline;" onsubmit="return confirm('¿Eliminar este libro?');">
                    <input type="hidden" name="accionLibro" value="eliminar">
                    <input type="hidden" name="idLibro" value="<%= libro.getId() %>">
                    <button type="submit">Eliminar</button>
                </form>
            </td>
        </tr>
        <%
                }
            } else {
        %>
        <tr>
            <td colspan="5">No hay libros registrados</td>
        </tr>
        <%
            }
        %>
    </table>
    
    <p>Total de Autores: <%= autores != null ? autores.size() : 0 %></p>
    <p>Total de Libros: <%= libros != null ? libros.size() : 0 %></p>
    
    <hr>
    <hr>
    
    <h2>Gestión de Estudiantes (CRUD Original)</h2>
    <form method="GET" action="SvEstudiante">
        <button type="submit">Ir al CRUD de Estudiantes</button>
    </form>
</body>
</html>