package com.neurondigital.selfcare.treatment.skincare;

public class SkinCareModel {

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


