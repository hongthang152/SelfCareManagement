package com.neurondigital.selfcare.service;

import android.content.Context;
import android.os.AsyncTask;

import com.neurondigital.selfcare.Configurations;
import com.neurondigital.selfcare.treatment.compressiontherapy.CTDatabase;
import com.neurondigital.selfcare.treatment.compressiontherapy.CTRecord;
import com.neurondigital.selfcare.treatment.exercise.ExerciseDatabase;
import com.neurondigital.selfcare.treatment.exercise.ExerciseModel;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDDatabase;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDModel;
import com.neurondigital.selfcare.treatment.pneumatic.PneumaticDatabase;
import com.neurondigital.selfcare.treatment.pneumatic.PneumaticModel;
import com.neurondigital.selfcare.treatment.skincare.SkinCareDatabase;
import com.neurondigital.selfcare.treatment.skincare.SkinCareModel;

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
    SkinCareDatabase scDB;
    PneumaticDatabase pneumaticDB;
    ExerciseDatabase exerciseDB;
    CTDatabase ctDB;
    AsyncResponse<JSONObject> response;

    public SyncAPI(Context ctx, AsyncResponse<JSONObject> asyncResponse) {
        this.context = ctx;
        this.mldDB = new MLDDatabase(this.context);
        this.scDB = new SkinCareDatabase(this.context);
        this.pneumaticDB = new PneumaticDatabase(this.context);
        this.exerciseDB = new ExerciseDatabase(this.context);
        this.ctDB = new CTDatabase(this.context);
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

            //Make json array of skincare records
            List<SkinCareModel> scList = scDB.getAllModels();
            JSONArray scJSONArray = new JSONArray();
            for(SkinCareModel scModel : scList) {
                scJSONArray.put(scModel.toJSONObject());
            }

            List<PneumaticModel> pneumaticModelList = pneumaticDB.getAll();
            JSONArray pnJSONArray = new JSONArray();
            for(PneumaticModel pnModel : pneumaticModelList) {
                pnJSONArray.put(pnModel.toJSONObject());
            }

            List<ExerciseModel> exerciseModelList = exerciseDB.getAll();
            JSONArray exeJSONArray = new JSONArray();
            for(ExerciseModel exeModel : exerciseModelList) {
                exeJSONArray.put(exeModel.toJSONObject());
            }

            List<CTRecord> ctList = ctDB.getAllCTRecords();
            JSONArray ctJSONArray = new JSONArray();
            for(CTRecord ctModel : ctList) {
                ctJSONArray.put(ctModel.toJSONObject());
            }

            jsonObject.put("mld", mldJSONArray);
            jsonObject.put("sc", scJSONArray);
            jsonObject.put("pcp", pnJSONArray);
            jsonObject.put("exe", exeJSONArray);
            jsonObject.put("ct", ctJSONArray);

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
