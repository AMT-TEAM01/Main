package ch.heig.vd;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    static String bucketService = "http://localhost:8080"; // TODO
    static String rekognitionService = "http://localhost:8081"; // TODO
    static String image = "street.jpg";
    static String result = "result.txt";

    public static void main(String[] args) {
        System.out.println("Hello world!");
        try {
            delRequest(bucketService + "/object?remote=" + image);
            delRequest(bucketService + "/object?remote=" + result);
            firstScenario();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void firstScenario(){
        try {
            uploadImage();
            String imageURL = getImageUrl();
            String json = analyzeImage(imageURL);
            uploadResult(json);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void uploadImage() throws IOException {
        byte[] bytes = Main.class.getResourceAsStream("/" + image).readAllBytes();
        String base64 = Base64.getUrlEncoder().encodeToString(bytes);
        Map<String, String> args = new HashMap<>();
        args.put("data", base64);
        args.put("remote", image);
        postRequest(bucketService + "/object", args);
    }

    public static String getImageUrl() throws IOException {
        String resp = getRequest(bucketService +  "/object-url?remote=" + image);
        System.out.println(resp);
        return resp;
    }

    public static String analyzeImage(String url) throws IOException {
        String resp = getRequest(rekognitionService + "/analyze?image=" + Base64.getUrlEncoder().encodeToString(url.getBytes()));
        System.out.println(resp);
        return resp;
    }

    public static void uploadResult(String json) throws IOException {
        Map<String, String> args = new HashMap<>();
        args.put("data", Base64.getUrlEncoder().encodeToString(json.getBytes()));
        args.put("remote", result);
        postRequest(bucketService + "/object", args);
    }

    public static void postRequest(String url, Map<String, String> args) throws IOException {
        System.out.println(url);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
        osw.write( args.entrySet()
                        .stream()
                        .map(e -> e.getKey() + "=" + e.getValue())
                        .collect(Collectors.joining("&")) );

        osw.flush();
        osw.close();
        os.close();
        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(response.toString());
        } else {
            System.out.println("POST request did not work, with response code : " + responseCode);
        }
    }

    public static String getRequest(String url) throws IOException {
        System.out.println(url);
        String resp;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            resp = response.toString();
        } else {
            resp = "GET request did not work, with response code : " + responseCode;
        }

        return resp;
    }

    public static String delRequest(String url) throws IOException {
        System.out.println(url);
        String resp;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("DELETE");
        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            resp = response.toString();
        } else {
            resp = "GET request did not work, with response code : " + responseCode;
        }

        return resp;
    }
}