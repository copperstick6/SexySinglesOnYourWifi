package com.example.ty.sexywifi;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

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

public class CallAPI extends AsyncTask<String, String, String[]>{ //

    final MatchActivity match;
    final String server = "http://231cd0aa.ngrok.io";
    public CallAPI(final MatchActivity match){
        this.match = match;
    }

    //http://95ac390b.ngrok.io/add_user?name=tytrusty&sex=F&latitude=90&longitude=100&SSID=1000&preference=M

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String[] doInBackground(String... params) {

        String choice = params[0];
        String request = "";
        HttpURLConnection urlConnection = null;
        URL url = null;
        String[] result = new String[2];

        if(choice.equals("add_user")){
            request = server + "/add_user?name=" + params[1] + "&sex=" + params[2] + "&latitude=" + params[3]
                    + "&longitude=" + params[4] + "&SSID=" + params[5] + "&preference=" + params[6];
        }
        else if(choice.equals("update_position")){
            request = server + "/updatePosition?name=" + params[1] + "&latitude=" + params[2] + "&longitude=" + params[3];
        }
        else if(choice.equals("update_sex")){
            request = server + "/updateSex?name=" + params[1] + "&sex=" + params[2];
        }
        else if(choice.equals("update_preference")){
            request = server + "/updatePreference?name=" + params[1] + "&preference=" + params[2];
        }
        else if(choice.equals("update_ssid")){
            request = server + "/updateSSID?name=" + params[1] + "&SSID=" + params[2];
        }
        else if(choice.equals("get_position")){
            request = server + "/getPosition?name=" + params[1];
        }
        else if(choice.equals("get_match")){
            request = server + "/getMatch?SSID=" + params[1] + "&name=" + params[2];
        }

        try {
            url = new URL(request);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String content = "";
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while((line = rd.readLine()) != null) {

                content += line + "\n";
            }
            System.out.println("HTTP results: " + content);

        } catch (IOException e) {
            e.printStackTrace();
        }

        result[0] = choice;
        result[1] = content;
        return result;
    }

    @Override
    protected void onPostExecute(String[] results){
        String choice = results[0];
        if(choice.equals("add_user")){
            match.updateDetails();
        }
        else if(choice.equals("update_position")){
           // UPDATE POSITION  N/A
        }
        else if(choice.equals("update_sex")){
            match.setSexUpdated(true);
            match.getMatch();
        }
        else if(choice.equals("update_preference")){
            match.setPrefUpdated(true);
            match.getMatch();
        }
        else if(choice.equals("update_ssid")){
            // I aint doing this shit fuck off
        }
        else if(choice.equals("get_position")){
            JSONObject jObject = null;

            String latitude = null;
            String longitude = null;
            try {
                jObject = new JSONObject(results[1]);
                latitude = jObject.getString("latitude");
                longitude = jObject.getString("longitude");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            match.setPosition(latitude, longitude);
        }
        else if(choice.equals("get_match")){
            JSONObject jObject = null;
            if (results[1].equals("No match found")) return;
            String matchName = null;
            try {
                System.out.println("results[1]:  " + results[1]);
                jObject = new JSONObject(results[1]);
                matchName = jObject.getString("name");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            match.setMatchName(matchName);
        }
    }
}
