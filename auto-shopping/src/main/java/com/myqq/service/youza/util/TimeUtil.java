package com.myqq.service.youza.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS");

    public static String getCurrentTimeString(){
        return dateTimeFormatter.format(LocalDateTime.now())+" ";
    }

    public static void main(String[] args) {
        System.out.println(getCurrentTimeString());
    }
}
