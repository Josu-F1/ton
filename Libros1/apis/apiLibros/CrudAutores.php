<?php
include_once "Conexion.php";

class CrudClientes
{
    public static function get()
    {
        $conexion = new Conexion();
        $conectar = $conexion->conectar();
        $sqlSelect = "SELECT * FROM AUTORES";
        $resultado = $conectar->prepare($sqlSelect);
        $resultado->execute();
        $data = $resultado->fetchAll(PDO::FETCH_ASSOC);
        echo json_encode($data);
    }

    public static function insert()
    {
        $conexion = new Conexion();
        $conectar = $conexion->conectar();
        $nombre = $_POST['NOMBRE_AUTOR'];
        $correo = $_POST['CORREO_AUTOR'];
        $sqlInsert = "INSERT INTO AUTORES (NOMBRE_AUTOR, CORREO_AUTOR) VALUES ('$nombre','$correo')";
        $resultado = $conectar->prepare($sqlInsert);
        $resultado->execute();
        $data = "Se insertó correctamente";
        echo json_encode($data);
    }

    public static function update()
    {
        $conexion = new Conexion();
        $conectar = $conexion->conectar();
        $id = $_GET['ID_AUTOR'];
        $nombre = $_GET['NOMBRE_AUTOR'];
        $correo = $_GET['CORREO_AUTOR'];
        $sqlUpdate = "UPDATE AUTORES SET NOMBRE_AUTOR='$nombre', CORREO_AUTOR='$correo' WHERE ID_AUTOR='$id'";
        $resultado = $conectar->prepare($sqlUpdate);
        $resultado->execute();
        $data = "Actualizado";
        echo json_encode($data);
    }

    public static function delete()
    {
        $conexion = new Conexion();
        $conectar = $conexion->conectar();
        if (isset($_GET['ID_AUTOR'])) {
            $id = $_GET['ID_AUTOR'];
        } else {
            http_response_code(400);
            echo json_encode(["error" => "Falta el parámetro 'ID_AUTOR'"]);
            return;
        }
        $sqlDelete = "DELETE FROM AUTORES WHERE ID_AUTOR='$id'";
        $resultado = $conectar->prepare($sqlDelete);
        $resultado->execute();
        $data = "Eliminado";
        echo json_encode($data);
    }

    public static function buscar()
    {
        $conexion = new Conexion();
        $conectar = $conexion->conectar();
        
        // Buscar por ID o por Nombre
        if (isset($_GET['ID_AUTOR'])) {
            $id = $_GET['ID_AUTOR'];
            $sqlConsulta = "SELECT * FROM AUTORES WHERE ID_AUTOR='$id'";
        } elseif (isset($_GET['NOMBRE_AUTOR'])) {
            $nombre = $_GET['NOMBRE_AUTOR'];
            $sqlConsulta = "SELECT * FROM AUTORES WHERE NOMBRE_AUTOR LIKE '%$nombre%'";
        } else {
            http_response_code(400);
            echo json_encode(["error" => "Debe proporcionar ID_AUTOR o NOMBRE_AUTOR"]);
            return;
        }
        
        $resultado = $conectar->prepare($sqlConsulta);
        $resultado->execute();
        $data = $resultado->fetchAll(PDO::FETCH_ASSOC);
        echo json_encode($data);
    }
}
