<%@page import ="org.json.JSONArray"%>
<%@page import ="org.json.JSONObject"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    
    if(request.getAttribute("estudiantes") == null){
        request.getRequestDispatcher("/SvEstudiante").forward(request, response);
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>CRUD Estudiantes</title>
    <script type="text/javascript">
        function confirmarEliminar(cedula) {
            if (confirm("Está seguro de querer eliminar al estudiante con cédula: " + cedula + "?")) {
                fetch("<%= request.getContextPath() %>/SvEstudiante?cedula=" + cedula,
                        {method: "DELETE"
                        }).then(response => {
                    if (response.ok) {
                        window.location.href = "<%= request.getContextPath()%>/estudiantes.jsp";
                    } else {
                        alert("Error al eliminar al estudiante.");
                    }
                }).catch(error => {
                    console.log("Error en delete: ", error);
                });
            }
        }
    </script>
</head>
<body>
    <h1>Gestión de Estudiantes</h1>
    <p><a href="index.jsp"><button type="button">Volver al Sistema de Autores y Libros</button></a></p>
    
    <h2>Lista de Estudiantes</h2>
    <table border ="1">
        <tr>
            <th>N# Est.</th>
            <th>Cedula</th>
            <th>Nombre</th>
            <th>Apellido</th>
            <th>Direccion</th>
            <th>Telefono</th>
            <th>Editar</th>
            <th>Eliminar</th>
        </tr>

        <% 
        JSONArray estudiantes = (JSONArray) request.getAttribute("estudiantes");
     
        if (estudiantes != null){
            for (int i=0; i<estudiantes.length();i++){
             JSONObject e= estudiantes.getJSONObject(i);
        %>
        <tr>
            <td><%= i+1%></td>
            <td><%= e.optString("cedula")%></td>
            <td><%= e.optString("nombre")%></td>
            <td><%= e.optString("apellido")%></td>
            <td><%=e.optString("direccion")%></td>
            <td><%= e.optString("telefono")%></td>
            <td>
                <form method="GET" action="<%= request.getContextPath() %>/editar.jsp">
                    <input type="hidden" name="cedula" value="<%= e.optString("cedula") %>">
                    <input type="hidden" name="nombre" value="<%= e.optString("nombre") %>">
                    <input type="hidden" name="apellido" value="<%= e.optString("apellido") %>">
                    <input type="hidden" name="direccion" value="<%= e.optString("direccion") %>">
                    <input type="hidden" name="telefono" value="<%= e.optString("telefono") %>">
                    <button type="submit">Editar</button>
                </form>
            </td>
            <td>
                <%
                    String cedulaEliminar = e.optString("cedula");
                %>
                <button type="button" onclick="confirmarEliminar('<%= cedulaEliminar %>')">Eliminar</button>
            </td>
        </tr>
        <%
            }
        }else{
            System.out.println("No se encontraron datos");
        %>
        <tr>
            <td colspan="8">No se encontraron estudiantes</td>
        </tr>
        <%
            }
        %>
    </table>
    
    <h2>Crear Estudiante</h2>
    <form method="post" action="${pageContext.request.contextPath}/SvEstudiante">
        <p><label>Cedula: <input name="cedula" required=""></label></p>
        <p><label>Nombre: <input name="nombre" required=""></label></p>
        <p><label>Apellido: <input name="apellido" required=""></label></p>
        <p><label>Direccion: <input name="direccion" required=""></label></p>
        <p><label>Telefono: <input name="telefono" required=""></label></p>
        <button type="submit">Guardar</button>
    </form>
</body>
</html>
