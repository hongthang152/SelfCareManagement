package com.neurondigital.selfcare.treatment.pneumatic;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PneumaticModel implements Serializable {
    public static final String DATE_FORMAT_STRING = "dd-MM-yyyy HH:mm:ss";
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT_STRING, Locale.ENGLISH);
    int id;
    String startTime;
    String duration;
    String endTime;

    public PneumaticModel(){

    }

    public PneumaticModel(int id, String startTime, String duration, String endTime) {
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = endTime;
    }
    public PneumaticModel( String startTime, String duration, String endTime) {
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = endTime;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("startTime", getStartTime());
            jsonObject.put("endTime", getEndTime());
            jsonObject.put("duration", getDuration());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }


    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}


