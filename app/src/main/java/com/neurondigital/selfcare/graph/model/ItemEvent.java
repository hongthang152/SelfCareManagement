package com.neurondigital.selfcare.graph.model;

import android.content.Intent;
import android.graphics.drawable.Drawable;

import java.text.SimpleDateFormat;

public class ItemEvent {
    public static final SimpleDateFormat TIME_SIMPLE_DATE_FORMAT = new SimpleDateFormat("h:mm a");

    private Drawable iconDrawable;
    private String activityDesc;
    private String startAndEnd;
    private Intent detailActivityIntent;

    public ItemEvent(Drawable iconDrawable, String activityDesc, String startAndEnd, Intent detailActivityIntent) {
        this.iconDrawable = iconDrawable;
        this.activityDesc = activityDesc;
        this.startAndEnd = startAndEnd;
        this.detailActivityIntent = detailActivityIntent;
    }

    public ItemEvent() {}

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public String getStartAndEnd() {
        return startAndEnd;
    }

    public void setStartAndEnd(String startAndEnd) {
        this.startAndEnd = startAndEnd;
    }

    public Intent getDetailActivityIntent() {
        return detailActivityIntent;
    }

    public void setDetailActivityIntent(Intent detailActivityIntent) {
        this.detailActivityIntent = detailActivityIntent;
    }
}
