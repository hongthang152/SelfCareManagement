package com.neurondigital.helpers;

import java.util.Date;

public class Utility {
    public static String diff(Date d1, Date d2) {
        long sec = Math.abs(d1.getTime() - d2.getTime());

        long hours = sec / (1000 * 60 * 60);
        long mins = (sec / 1000 / 60) % 60;
        long secs = (sec / 1000) % 60;

        StringBuilder builder = new StringBuilder();
        if(hours > 0) {
            if(hours < 10) builder.append("0");
            builder.append(hours).append(":");
        }

        if(mins > 0) {
            if(mins < 10) builder.append("0");
            builder.append(mins);
        } else {
            builder.append("00");
        }
        builder.append(":");
        if(secs > 0) {
            if(secs < 10) builder.append("0");
            builder.append(secs);
        } else {
            builder.append("00");
        }
        return builder.toString();
    }
}
