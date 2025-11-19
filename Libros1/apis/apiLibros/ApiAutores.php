<?php
include_once "CrudAutores.php";
$opc = $_SERVER['REQUEST_METHOD'];

switch ($opc) {
  case 'GET':
    if (isset($_GET['ID_AUTOR']) || isset($_GET['NOMBRE_AUTOR'])) {
      CrudClientes::buscar();
    } else {
      CrudClientes::get();
    }
    break;
  case 'POST':
    CrudClientes::insert();
    break;
  case 'PUT':
    CrudClientes::update();
    break;
  case 'DELETE':
    CrudClientes::delete();
    break;
  default:
    echo json_encode(["error" => "Método no soportado"]);
}
