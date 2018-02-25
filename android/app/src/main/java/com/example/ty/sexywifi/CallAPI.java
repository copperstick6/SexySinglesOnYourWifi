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

        String choice = params[0];
        String request = "";
        HttpURLConnection urlConnection = null;
        URL url = null;

        if(choice.equals("add_user")){
            request = "http://95ac390b.ngrok.io/add_user?name=" + params[1] + "&sex=" + params[2] + "&latitude=" + params[3]
                    + "&longitude=" + params[4] + "&SSID=" + params[5] + "&preference=" + params[6];
        }
        else if(choice.equals("update_position")){
            request = "http://95ac390b.ngrok.io/updatePosition?name=" + params[1] + "&latitude=" + params[2] + "&longitude=" + params[3];
        }
        else if(choice.equals("update_sex")){
            request = "http://95ac390b.ngrok.io/updateSex?name=" + params[1] + "&sex=" + params[2];
        }
        else if(choice.equals("update_preference")){
            request = "http://95ac390b.ngrok.io/updatePreference?name=" + params[1] + "&preference=" + params[2];
        }
        else if(choice.equals("update_ssid")){
            request = "http://95ac390b.ngrok.io/updateSSID?name=" + params[1] + "&SSID=" + params[2];
        }
        else if(choice.equals("get_position")){
            request = "http://95ac390b.ngrok.io/getPosition?name=" + params[1];
        }
        else if(choice.equals("get_match")){
            request = "http://95ac390b.ngrok.io/getMatch?SSID=" + params[1] + "&name=" + params[2];
        }

        try {
            url = new URL(request);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

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

        return "";
    }
}
