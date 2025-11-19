  <?php
  include_once "CrudLibros.php";
  $opc = $_SERVER['REQUEST_METHOD'];

  switch ($opc) {
    case 'GET':
      if (isset($_GET['TITULO_LIBRO']) || isset($_GET['ID_LIBRO'])) {
        CrudLibros::buscar();
      } else {
        CrudLibros::get();
      }
      break;
    case 'POST':
      CrudLibros::insert();
      break;
    case 'PUT':
      CrudLibros::update();
      break;
    case 'DELETE':
      CrudLibros::delete();
      break;
    default:
      echo json_encode(["error" => "Método no soportado"]);
  }
