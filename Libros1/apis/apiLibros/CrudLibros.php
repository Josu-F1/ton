<?php
include_once "Conexion.php";

class CrudLibros
{

    public static function get()
    {

        //$sql = "SELECT l.ID_LIBRO, l.TITULO_LIBRO, a.NOMBRE_AUTOR AS AUTOR, l.ANIO_LIBRO
        //        FROM LIBROS l
        //        JOIN AUTORES a ON l.AUTOR_ID_PER = a.ID_AUTOR";
        $sql = "SELECT l.ID_LIBRO, l.TITULO_LIBRO, l.AUTOR_ID_PER, a.NOMBRE_AUTOR AS AUTOR, l.ANIO_LIBRO 
        FROM LIBROS l JOIN AUTORES a ON l.AUTOR_ID_PER = a.ID_AUTOR";
        try {
            $conexion = new Conexion();
            $conectar = $conexion->conectar();
            $resultado = $conectar->prepare($sql);
            $resultado->execute();
            $data = $resultado->fetchAll(PDO::FETCH_ASSOC);
            echo json_encode($data);
        } catch (PDOException $e) {
            echo json_encode(["error" => "Error al obtener libro: " . $e->getMessage()]);
        }
    }

    public static function buscar()
    {
        $conexion = new Conexion();
        $conectar = $conexion->conectar();
        
        // Buscar por ID o por Título
        if (isset($_GET['ID_LIBRO'])) {
            $id = $_GET['ID_LIBRO'];
            $sqlConsulta = "SELECT l.ID_LIBRO, l.TITULO_LIBRO, l.AUTOR_ID_PER, a.NOMBRE_AUTOR AS AUTOR, l.ANIO_LIBRO 
                            FROM LIBROS l 
                            JOIN AUTORES a ON l.AUTOR_ID_PER = a.ID_AUTOR 
                            WHERE l.ID_LIBRO='$id'";
        } elseif (isset($_GET['TITULO_LIBRO'])) {
            $titulo = $_GET['TITULO_LIBRO'];
            $sqlConsulta = "SELECT l.ID_LIBRO, l.TITULO_LIBRO, l.AUTOR_ID_PER, a.NOMBRE_AUTOR AS AUTOR, l.ANIO_LIBRO 
                            FROM LIBROS l 
                            JOIN AUTORES a ON l.AUTOR_ID_PER = a.ID_AUTOR 
                            WHERE l.TITULO_LIBRO LIKE '%$titulo%'";
        } else {
            http_response_code(400);
            echo json_encode(["error" => "Debe proporcionar ID_LIBRO o TITULO_LIBRO"]);
            return;
        }

        $resultado = $conectar->prepare($sqlConsulta);
        $resultado->execute();
        $data = $resultado->fetchAll(PDO::FETCH_ASSOC);
        echo json_encode($data);
    }

    public static function insert()
    {
        $titulo = $_POST['TITULO_LIBRO'];
        $id_autor = $_POST['AUTOR_ID_PER'];
        $anio = $_POST['ANIO_LIBRO'];
        $conexion = new Conexion();
        $conectar = $conexion->conectar();
        $sqlInsert = "INSERT INTO LIBROS (TITULO_LIBRO, AUTOR_ID_PER, ANIO_LIBRO ) VALUES ('$titulo','$id_autor', '$anio')";
        $resultado = $conectar->prepare($sqlInsert);
        $resultado->execute();
        $data = "Se insertó correctamente";
        echo json_encode($data);
    }

    public static function update()
    {

        $id_libro = $_GET['ID_LIBRO'];
        $titulo = $_GET['TITULO_LIBRO'];
        $id_autor = $_GET['AUTOR_ID_PER'];
        $anio = $_GET['ANIO_LIBRO'];
        $conexion = new Conexion();
        $conectar = $conexion->conectar();
        $sqlUpdate = "UPDATE LIBROS SET TITULO_LIBRO='$titulo', AUTOR_ID_PER='$id_autor', ANIO_LIBRO='$anio' WHERE ID_LIBRO='$id_libro'";
        $resultado = $conectar->prepare($sqlUpdate);
        $resultado->execute();
        $data = "Actualizado";
        echo json_encode($data);
    }

    public static function delete()
    {
        $conexion = new Conexion();
        $conectar = $conexion->conectar();
        if (isset($_GET['ID_LIBRO'])) {
            $id = $_GET['ID_LIBRO'];
        } else {
            http_response_code(400);
            echo json_encode(["error" => "Falta el parámetro 'ID_LIBRO'"]);
            return;
        }
        $sqlDelete = "DELETE FROM LIBROS WHERE ID_LIBRO='$id'";
        $resultado = $conectar->prepare($sqlDelete);
        $resultado->execute();
        $data = "Eliminado";
        echo json_encode($data);
    }
}
