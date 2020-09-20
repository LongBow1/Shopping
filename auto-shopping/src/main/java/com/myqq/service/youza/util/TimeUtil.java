package com.myqq.service.youza.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS");

    public static String getCurrentTimeString(){
        return dateTimeFormatter.format(LocalDateTime.now())+" ";
    }

    public static String getTimeString(long time){
        return dateTimeFormatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
    }

    public static void main(String[] args) {
        System.out.println(getCurrentTimeString());
        System.out.println(getTimeString(1600603500000L));
    }
}
