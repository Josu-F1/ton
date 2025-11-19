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

/**
 *
 * @author USER
 */
public class EstudianteCliente {

    private static final String API_URL = "http://localhost:8081/expo/SvEstudiante";
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    public static JSONArray getEstudiantes() {
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
            System.out.println("Error" + e);
        }
        return null;
    }

    public static boolean crearEstudiante(String cedula, String nombre, String apellido, String direccion, String telefono) {
        try {
            String json = String.format("{\"cedula\":\"%s\",\"nombre\":\"%s\",\"apellido\":\"%s\",\"direccion\":\"%s\",\"telefono\":\"%s\"}", 
                    cedula, nombre, apellido, direccion, telefono);
            
            HttpRequest peticion = HttpRequest.newBuilder(new URI(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> respuesta = HTTP.send(peticion, HttpResponse.BodyHandlers.ofString());

            return (respuesta.statusCode()) == 200;

        } catch (Exception e) {
            System.out.println("Error creando estudiante: " + e.getMessage());
            return false;
        }
    }

    public static boolean eliminarEstudiante(String cedula) {
        String url = API_URL + "?cedula=" + cedula;

        try {
            HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                    .DELETE()
                    .header("Accept", "application/json")
                    .build();
            HttpResponse<String> res = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
            return (res.statusCode()) == 200;
        } catch (Exception ex) {
            return false;

        }

    }

    public static boolean editarEstudiante(String cedula, String nombre, String apellido, String direccion, String telefono) {
        try {
            String json = String.format("{\"cedula\":\"%s\",\"nombre\":\"%s\",\"apellido\":\"%s\",\"direccion\":\"%s\",\"telefono\":\"%s\"}", 
                    cedula, nombre, apellido, direccion, telefono);
            
            HttpRequest request = HttpRequest.newBuilder(new URI(API_URL))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;

        } catch (Exception e) {
            System.out.println("Error editando estudiante: " + e.getMessage());
            return false;
        }
    }


}
