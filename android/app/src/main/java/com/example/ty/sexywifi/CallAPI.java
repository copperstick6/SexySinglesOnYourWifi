package com.example.ty.sexywifi;

import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class CallAPI extends AsyncTask<String, String, String>{ //

    public CallAPI(){

    }

    //http://95ac390b.ngrok.io/add_user?name=tytrusty&sex=F&latitude=90&longitude=100&SSID=1000&preference=M

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {
        String request = "http://95ac390b.ngrok.io/add_user?name=" + params[0] + "&sex=" + params[1] + "&latitude=" + params[2]
                + "&longitude=" + params[3] + "&SSID=" + params[4] + "&preference=" + params[5];

        URL url = null;
        try {
            url = new URL(request);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String content = "", line;
            while((line = rd.readLine()) != null) {

                content += line + "\n";
            }
            System.out.println("HTTP results: " + content);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //urlConnection.setDoOutput(true);

        return "";
    }
}
