package com.neurondigital.selfcare;

public class ExeModel {

    int id;
    String date;
    String savedtime;

    public ExeModel() {
    }

    public ExeModel(int id, String date, String savedtime) {
        this.id = id;
        this.date = date;
        this.savedtime = savedtime;
    }

    public ExeModel(String date, String savedtime) {
        this.date = date;
        this.savedtime = savedtime;
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

    public String getSavedtime() {
        return this.savedtime;
    }

    public void setSavedtime(String savedtime) {
        this.savedtime = savedtime;
    }
}

