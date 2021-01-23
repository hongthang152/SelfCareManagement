package com.neurondigital.selfcare;
public class MeasureClass {

    int id;
    String date;


    public MeasureClass() {
    }

    public MeasureClass(int id, String type, String measure) {
        this.id = id;
        this.date = type;
    }

    public MeasureClass(String date) {
        this.date = date;

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

    public void setType(String date) {
        this.date = date;
    }


}
