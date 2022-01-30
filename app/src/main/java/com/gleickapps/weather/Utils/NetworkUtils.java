package com.gleickapps.weather.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

    // Responsável por carregar o objeto JSON
    public static String getJSONFromAPI(String url) throws IOException {
        String result = "";
        try {
            URL apiEnd = new URL(url);
            int responseCode;
            HttpURLConnection connection;
            InputStream is;

            connection = (HttpURLConnection) apiEnd.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.connect();

            responseCode = connection.getResponseCode();
            if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                is = connection.getInputStream();
            } else {
                is = connection.getErrorStream();
            }

            result = converterInputStreamToString(is);
            is.close();
            connection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static String converterInputStreamToString(InputStream is){
        StringBuilder buffer = new StringBuilder();
        try {
            BufferedReader br;
            String line;

            br = new BufferedReader(new InputStreamReader(is));
            while((line = br.readLine())!=null){
                buffer.append(line);
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }
}
