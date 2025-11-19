<%-- 
    Document   : editar
    Created on : 18 nov. 2025
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Editar Estudiante</title>
    </head>
    <body>
        <div class="container">
            <h2>Editar Estudiante</h2>
            
            <form id="editarForm">
                <div class="form-group">
                    <label for="cedula">Cédula:</label>
                    <input type="text" id="cedula" name="cedula" value="<%= request.getParameter("cedula") %>" readonly>
                </div>
                
                <div class="form-group">
                    <label for="nombre">Nombre:</label>
                    <input type="text" id="nombre" name="nombre" value="<%= request.getParameter("nombre") %>" required>
                </div>
                
                <div class="form-group">
                    <label for="apellido">Apellido:</label>
                    <input type="text" id="apellido" name="apellido" value="<%= request.getParameter("apellido") %>" required>
                </div>
                
                <div class="form-group">
                    <label for="direccion">Dirección:</label>
                    <input type="text" id="direccion" name="direccion" value="<%= request.getParameter("direccion") %>" required>
                </div>
                
                <div class="form-group">
                    <label for="telefono">Teléfono:</label>
                    <input type="text" id="telefono" name="telefono" value="<%= request.getParameter("telefono") %>" required>
                </div>
                
                <button type="submit">Actualizar</button>
                <button type="button" class="btn-secondary" onclick="cancelar()">Cancelar</button>
            </form>
        </div>

        <script>
            document.getElementById('editarForm').addEventListener('submit', function(e) {
                e.preventDefault();
                
                const estudiante = {
                    cedula: document.getElementById('cedula').value,
                    nombre: document.getElementById('nombre').value,
                    apellido: document.getElementById('apellido').value,
                    direccion: document.getElementById('direccion').value,
                    telefono: document.getElementById('telefono').value
                };
                
                fetch('<%= request.getContextPath() %>/SvEstudiante', {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(estudiante)
                })
                .then(response => response.json())
                .then(data => {
                    if (data.actualizado) {
                        alert('Estudiante actualizado correctamente');
                        window.location.href = '<%= request.getContextPath() %>/SvEstudiante';
                    } else {
                        alert('Error al actualizar el estudiante');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Error al actualizar el estudiante');
                });
            });
            
            function cancelar() {
                window.location.href = '<%= request.getContextPath() %>/SvEstudiante';
            }
        </script>
    </body>
</html>