package com.neurondigital.selfcare.service;

import android.content.Context;
import android.os.AsyncTask;

import com.neurondigital.selfcare.Configurations;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDDatabase;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDModel;

import org.json.JSONArray;
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
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.neurondigital.selfcare.service.AuthenticationAPI.AUTHENTICATION_SHARED_PREF;

public class SyncAPI extends AsyncTask<String, String, JSONObject> {
    Context context;
    MLDDatabase mldDB;
    AsyncResponse<JSONObject> response;

    public SyncAPI(Context ctx, AsyncResponse<JSONObject> asyncResponse) {
        this.context = ctx;
        this.mldDB = new MLDDatabase(this.context);
        this.response = asyncResponse;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        try {
            URL url = new URL(Configurations.SERVER_URL + "sync");
            String bearerAuth = "Bearer " + context.getSharedPreferences(AUTHENTICATION_SHARED_PREF, MODE_PRIVATE).getString("token", null);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty ("Authorization", bearerAuth);
            httpURLConnection.connect();

            List<MLDModel> mldList = mldDB.getAll();
            JSONObject jsonObject = new JSONObject();
            JSONArray mldJSONArray = new JSONArray();

            for(MLDModel mld : mldList) {
                mldJSONArray.put(mld.toJSONObject());
            }

            jsonObject.put("mld", mldJSONArray);

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

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        if(this.response != null) {
            this.response.processFinish(jsonObject);
        }
    }
}
