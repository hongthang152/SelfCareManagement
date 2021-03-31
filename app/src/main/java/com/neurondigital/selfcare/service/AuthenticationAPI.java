package com.neurondigital.selfcare.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.neurondigital.selfcare.Configurations;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class AuthenticationAPI extends AsyncTask<String, String, JSONObject> {

    public static String AUTHENTICATION_SHARED_PREF = "authentication",
        TOKEN_SHARED_PREEF_KEY = "token";

    Context context;
    AsyncResponse response;

    public AuthenticationAPI(Context context, AsyncResponse<JSONObject> response) {
        this.context = context; this.response = response;
    }

    public static boolean isAuthenticated(Context context) {
        return context.getSharedPreferences(AUTHENTICATION_SHARED_PREF, MODE_PRIVATE).getString(TOKEN_SHARED_PREEF_KEY, null) != null;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            URL url = new URL(Configurations.SERVER_URL + "login");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.connect();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", params[0]);
            jsonObject.put("password", params[1]);

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(jsonObject.toString());
            wr.flush();
            wr.close();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuilder result = new StringBuilder();
                InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return new JSONObject(result.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(JSONObject result) {
        try {
            String token = result.getString("token");
            SharedPreferences.Editor editor = context.getSharedPreferences(AUTHENTICATION_SHARED_PREF, MODE_PRIVATE).edit();
            editor.putString(TOKEN_SHARED_PREEF_KEY, token);
            editor.apply();
            if(this.response != null) {
                this.response.processFinish(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
