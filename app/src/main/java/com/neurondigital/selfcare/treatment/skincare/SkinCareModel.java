package com.neurondigital.selfcare.treatment.skincare;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SkinCareModel {
    public static final String DATE_FORMAT_STRING = "dd-MM-yyyy HH:mm:ss";
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT_STRING, Locale.ENGLISH);


    int id;
    String date;
    String savedtime;
    String note;

    public SkinCareModel() {
    }

    public SkinCareModel(int id, String date, String note) {
        this.id = id;
        this.date = date;
        this.note = note;

    }

    public SkinCareModel(String date, String note) {
        this.date = date;
        this.savedtime = savedtime;
        this.note = note;

    }

    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}


