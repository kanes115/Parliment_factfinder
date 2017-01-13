package zad.two.mvn;

import zad.two.mvn.exceptions.DownloaderException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kanes on 24.12.2016.
 */
public class Downloader {
    private String URL;
    private String responseText;
    private int responseCode;

    public Downloader(String url) {
        URL = url;
    }

    private void sendGetRequest() throws IOException {

        java.net.URL objUrl = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) objUrl.openConnection();

        // Ustawienie metody żądania na GET
        connection.setRequestMethod("GET");

        // Pobranie kodu odpowiedzi
        responseCode = connection.getResponseCode();

        // Pobranie odpowiedzi
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        // Pobieramy odpowiedź linia po linii
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line + "\n");
        }

        reader.close();

        responseText = response.toString();

    }

    public String getResponseText() {
        return responseText;
    }

    public String sendRequestAndGetResponse(){
        try {
            sendGetRequest();
            return responseText;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setURL(String url) throws DownloaderException {
        try {
            URL = url;
            sendGetRequest();
        } catch (IOException e) {
            throw new DownloaderException("Error while downloading jsons. \n" + e.getMessage());
        }
    }
}