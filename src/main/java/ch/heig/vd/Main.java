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
        try {
            delRequest(bucketService + "/object?remote=" + image);
            delRequest(bucketService + "/object?remote=" + result);
            uploadImage();
            String imageURL = getImageUrl();
            String json = analyzeImage(imageURL);
            System.out.println("Analysis result : " + json);
            uploadResult(json);
            System.out.println("It works");
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
        if (!postRequest(bucketService + "/object", args)) {
            System.out.println("Couldn't upload the image");
        }
    }

    public static String getImageUrl() throws IOException {
        String resp = getRequest(bucketService +  "/object-url?remote=" + image);
        if (resp == null) {
            throw new RuntimeException("Image URL couldn't be retrieved");
        }
        return resp;
    }

    public static String analyzeImage(String url) throws IOException {
        String resp = getRequest(rekognitionService + "/analyze?image=" + Base64.getUrlEncoder().encodeToString(url.getBytes()));
        if (resp == null) {
            throw new RuntimeException("Image couldn't be analyzed, the response json wasn't retrieved");
        }
        return resp;
    }

    public static void uploadResult(String json) throws IOException {
        Map<String, String> args = new HashMap<>();
        args.put("data", Base64.getUrlEncoder().encodeToString(json.getBytes()));
        args.put("remote", result);
        if (!postRequest(bucketService + "/object", args)) {
            throw new RuntimeException("Couldn't upload the result");
        }
    }

    public static boolean postRequest(String url, Map<String, String> args) throws IOException {
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

        if (responseCode != HttpURLConnection.HTTP_OK) {
            System.out.println("POST request did not work, with error: " + responseCode + " : " + con.getResponseMessage());
            return false;
        }

        return true;
    }

    public static String getRequest(String url) throws IOException {
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
            System.out.println("GET request did not work, with error: " + responseCode + " : " + con.getResponseMessage());
            return null;
        }

        return resp;
    }

    public static void delRequest(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("DELETE");
        int responseCode = con.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_OK) {
           System.out.println("DEL request did not work, with error: " + responseCode + " : " + con.getResponseMessage());
        }

    }
}