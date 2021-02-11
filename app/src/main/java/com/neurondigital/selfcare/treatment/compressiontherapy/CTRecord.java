package com.neurondigital.selfcare.treatment.compressiontherapy;

import java.io.Serializable;

public class CTRecord implements Serializable {

    private int id;
    private String daynightTime;
    private String name;
    private String startTime;
    private String duration;
    private String endTime;

    public CTRecord() {
    }

    public CTRecord(int id, String daynightTime, String name, String startTime, String duration, String endTime) {
        this.id = id;
        this.daynightTime = daynightTime;
        this.name = name;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = endTime;
    }

    public CTRecord(String daynightTime, String name, String startTime, String duration, String endTime) {
        this.daynightTime = daynightTime;
        this.name = name;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDaynightTime() {
        return daynightTime;
    }

    public void setDaynightTime(String daynightTime) {
        this.daynightTime = daynightTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDuration() {
        return duration;
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
