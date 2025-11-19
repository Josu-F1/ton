/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.uta.grupo4.expo.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author USER
 */
public class LibroCliente {

    private static final String API_URL = "http://localhost:8081/expo/api/libro";
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    public static JSONArray getLibros() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            InputStreamReader lector = new InputStreamReader(conn.getInputStream(), "UTF-8");
            BufferedReader bf = new BufferedReader(lector);
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = bf.readLine()) != null) {
                sb.append(linea);
            }
            bf.close();
            lector.close();
            conn.disconnect();
            return new JSONArray(sb.toString());
        } catch (Exception e) {
            System.out.println("Error obteniendo libros: " + e);
        }
        return null;
    }

    public static JSONObject getLibroPorId(int id) {
        try {
            URL url = new URL(API_URL + "?id=" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            InputStreamReader lector = new InputStreamReader(conn.getInputStream(), "UTF-8");
            BufferedReader bf = new BufferedReader(lector);
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = bf.readLine()) != null) {
                sb.append(linea);
            }
            bf.close();
            lector.close();
            conn.disconnect();
            return new JSONObject(sb.toString());
        } catch (Exception e) {
            System.out.println("Error obteniendo libro: " + e);
        }
        return null;
    }

    public static JSONArray getLibrosPorAutor(int autorId) {
        try {
            URL url = new URL(API_URL + "?autorId=" + autorId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            InputStreamReader lector = new InputStreamReader(conn.getInputStream(), "UTF-8");
            BufferedReader bf = new BufferedReader(lector);
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = bf.readLine()) != null) {
                sb.append(linea);
            }
            bf.close();
            lector.close();
            conn.disconnect();
            return new JSONArray(sb.toString());
        } catch (Exception e) {
            System.out.println("Error obteniendo libros por autor: " + e);
        }
        return null;
    }

    public static boolean crearLibro(String titulo, String anioPublicacion, int autorId) {
        try {
            String json = String.format("{\"titulo\":\"%s\",\"anioPublicacion\":\"%s\",\"autorId\":%d}", 
                    titulo, anioPublicacion, autorId);
            
            HttpRequest peticion = HttpRequest.newBuilder(new URI(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> respuesta = HTTP.send(peticion, HttpResponse.BodyHandlers.ofString());

            return (respuesta.statusCode()) == 201 || (respuesta.statusCode()) == 200;

        } catch (Exception e) {
            System.out.println("Error creando libro: " + e.getMessage());
            return false;
        }
    }

    public static boolean editarLibro(int id, String titulo, String anioPublicacion, int autorId) {
        try {
            String json = String.format("{\"id\":%d,\"titulo\":\"%s\",\"anioPublicacion\":\"%s\",\"autorId\":%d}", 
                    id, titulo, anioPublicacion, autorId);
            
            HttpRequest request = HttpRequest.newBuilder(new URI(API_URL))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;

        } catch (Exception e) {
            System.out.println("Error editando libro: " + e.getMessage());
            return false;
        }
    }

    public static boolean eliminarLibro(int id) {
        String url = API_URL + "?id=" + id;

        try {
            HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                    .DELETE()
                    .header("Accept", "application/json")
                    .build();
            HttpResponse<String> res = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
            return (res.statusCode()) == 200;
        } catch (Exception ex) {
            System.out.println("Error eliminando libro: " + ex.getMessage());
            return false;
        }
    }
}
