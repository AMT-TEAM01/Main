package ch.heig.vd;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    static String bucketService = "http://localhost:8080"; // TODO
    static String rekognitionService = "http://localhost:8081"; // TODO
    static String testImage = "https://upload.wikimedia.org/wikipedia/commons/3/39/Typical_Street_In_The_Royal_Borough_Of_Kensington_And_Chelsea_In_London.jpg";
    static String image = "street.jpg";

    public static void main(String[] args) {
        System.out.println("Hello world!");
        try {
            firstScenario();
            secondScenario();
            thirdScenario();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void firstScenario(){
        try {
            Map<String, String> args = new HashMap<>();
            args.put("data", "02");
            args.put("remote", image);
            postRequest(bucketService + "/object", args);
            System.out.println(getRequest(rekognitionService, "/analyze?image=" + testImage));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void secondScenario() {

    }

    public static void thirdScenario() {

    }

    public static void postRequest(String url, Map<String, String> args) throws IOException {
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
            System.out.println("GET request did not work, with response code : " + responseCode);
        }
    }

    public static String getRequest(String url, String arguments) throws IOException {
        String resp;
        URL obj = new URL(url + arguments);
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
}