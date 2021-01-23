package com.neurondigital.selfcare;

public class MLDModel {

        int id;
        String date;
        String savedtime;

        public MLDModel() {
        }

        public MLDModel(int id, String date, String savedtime) {
            this.id = id;
            this.date = date;
            this.savedtime = savedtime;
        }

        public MLDModel(String date, String savedtime) {
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

