package ec.edu.uta.grupo4.librosjavawebver2.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;

/**
 * Cliente para consumir APIs de PHP (Libros y Autores)
 */
public class ApiCliente {

    private static final String URL_LIBROS = "http://localhost/apiLibros/ApiLibros.php";
    private static final String URL_AUTORES = "http://localhost/apiLibros/ApiAutores.php";
    private static final HttpClient cliente = HttpClient.newHttpClient();


    private static JSONArray getDatos(String urlApi) {
        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlApi);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return new JSONArray(); // Retorna array vacío si falla
            }

            try (InputStreamReader lector = new InputStreamReader(conn.getInputStream(), "UTF-8"); BufferedReader br = new BufferedReader(lector)) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    sb.append(linea.trim());
                }
            }
            return new JSONArray(sb.toString());
        } catch (Exception ex) {
            Logger.getLogger(ApiCliente.class.getName()).log(Level.SEVERE, "Error GET: " + urlApi, ex);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return new JSONArray();
    }

    public static JSONArray getLibros() {
        return getDatos(URL_LIBROS);
    }

    public static JSONArray getAutores() {
        return getDatos(URL_AUTORES);
    }

    // ==========================================
    //              LOGICA LIBROS
    // ==========================================
    public static boolean crearLibro(String titulo, String idAutor, String anio) {
        try {
            // PHP espera $_POST: TITULO_LIBRO, AUTOR_ID_PER, ANIO_LIBRO
            String parametros = "TITULO_LIBRO=" + URLEncoder.encode(titulo, StandardCharsets.UTF_8)
                    + "&AUTOR_ID_PER=" + URLEncoder.encode(idAutor, StandardCharsets.UTF_8)
                    + "&ANIO_LIBRO=" + URLEncoder.encode(anio, StandardCharsets.UTF_8);

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI(URL_LIBROS))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(parametros))
                    .build();

            HttpResponse<String> res = cliente.send(req, BodyHandlers.ofString());
            return res.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean actualizarLibro(String idLibro, String titulo, String idAutor, String anio) {
        try {
            // PHP espera $_GET en el UPDATE, enviamos params en la URL
            String query = "?ID_LIBRO=" + URLEncoder.encode(idLibro, StandardCharsets.UTF_8)
                    + "&TITULO_LIBRO=" + URLEncoder.encode(titulo, StandardCharsets.UTF_8)
                    + "&AUTOR_ID_PER=" + URLEncoder.encode(idAutor, StandardCharsets.UTF_8)
                    + "&ANIO_LIBRO=" + URLEncoder.encode(anio, StandardCharsets.UTF_8);

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI(URL_LIBROS + query))
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> res = cliente.send(req, BodyHandlers.ofString());
            return res.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarLibro(String idLibro) {
        try {
            String query = "?ID_LIBRO=" + idLibro;
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI(URL_LIBROS + query))
                    .DELETE()
                    .build();

            HttpResponse<String> res = cliente.send(req, BodyHandlers.ofString());
            return res.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

// ==========================================
    //              LOGICA AUTORES
    // ==========================================
    public static boolean crearAutor(String nombre, String correo) {
        try {
            // PHP insert espera $_POST
            String parametros = "NOMBRE_AUTOR=" + URLEncoder.encode(nombre, StandardCharsets.UTF_8)
                    + "&CORREO_AUTOR=" + URLEncoder.encode(correo, StandardCharsets.UTF_8);

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI(URL_AUTORES))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(parametros))
                    .build();
            HttpResponse<String> res = cliente.send(req, BodyHandlers.ofString());
            return res.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean actualizarAutor(String id, String nombre, String correo) {
        try {
            // PHP update espera $_GET en la URL
            String query = "?ID_AUTOR=" + URLEncoder.encode(id, StandardCharsets.UTF_8)
                    + "&NOMBRE_AUTOR=" + URLEncoder.encode(nombre, StandardCharsets.UTF_8)
                    + "&CORREO_AUTOR=" + URLEncoder.encode(correo, StandardCharsets.UTF_8);

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI(URL_AUTORES + query))
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> res = cliente.send(req, BodyHandlers.ofString());
            return res.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarAutor(String id) {
        try {
            // PHP delete espera $_GET['ID_AUTOR']
            String query = "?ID_AUTOR=" + id;
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI(URL_AUTORES + query))
                    .DELETE()
                    .build();

            HttpResponse<String> res = cliente.send(req, BodyHandlers.ofString());
            return res.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    public static JSONArray buscarLibrosPorTitulo(String titulo) {
        try {
            String query = "?TITULO_LIBRO=" + URLEncoder.encode(titulo, StandardCharsets.UTF_8);
            // Reutilizamos el método getDatos genérico que creamos antes
            return getDatos(URL_LIBROS + query);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    // BUSCADOR DE LIBROS (Por ID)
    public static JSONArray buscarLibrosPorId(String id) {
        try {
            String query = "?ID_LIBRO=" + URLEncoder.encode(id, StandardCharsets.UTF_8);
            return getDatos(URL_LIBROS + query);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    // BUSCADOR DE AUTORES (Por ID)
    public static JSONArray buscarAutorPorId(String id) {
        try {
            String query = "?ID_AUTOR=" + URLEncoder.encode(id, StandardCharsets.UTF_8);
            return getDatos(URL_AUTORES + query);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    // BUSCADOR DE AUTORES (Por Nombre)
    public static JSONArray buscarAutorPorNombre(String nombre) {
        try {
            String query = "?NOMBRE_AUTOR=" + URLEncoder.encode(nombre, StandardCharsets.UTF_8);
            return getDatos(URL_AUTORES + query);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }
}
