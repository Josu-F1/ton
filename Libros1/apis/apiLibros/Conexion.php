<?php
  class Conexion {
    public function conectar() {
      $servername = "localhost:3306";
      $dsn = "mysql:host=$servername;dbname=libros";
      $username = "root";
      $password = "";

      try {
        $conn = new PDO($dsn, $username, $password);
      } catch(Exception $e) {
        die("Fallo: " . $e->getMessage());
      }
      return $conn;
    }
  }
?>