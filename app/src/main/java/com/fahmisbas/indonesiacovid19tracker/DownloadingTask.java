package com.fahmisbas.indonesiacovid19tracker;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadingTask {

    private String address;
    private String result = "";

    public String getResult() {
        return result;
    }

    private void setResult(String result) {
        this.result = result;
    }

    public DownloadingTask(String address) {
        this.address = address;
    }

    public void start(){
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream in = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();

            while (data != -1) {
                char current = (char) data;
                result+=current;
                data = reader.read();
            }

            setResult(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}