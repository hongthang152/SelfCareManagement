package com.neurondigital.selfcare.treatment.exercise;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ExerciseModel implements Serializable {
    public static final String DATE_FORMAT_STRING = "dd-MM-yyyy HH:mm:ss";
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT_STRING, Locale.ENGLISH);

    int id;
    String name;
    String startTime;
    String duration;
    String endTime;

    public ExerciseModel() {
    }

    public ExerciseModel(int id, String name,String startTime, String duration, String endTime) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = endTime;
    }

    public ExerciseModel(String name,String startTime, String duration, String endTime) {
        this.name = name;
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
            jsonObject.put("name", getName());
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

