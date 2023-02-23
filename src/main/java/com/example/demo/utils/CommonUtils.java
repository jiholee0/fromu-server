package com.example.demo.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;


public class CommonUtils {
    public static String createUserCode(){
        String userCode = RandomStringUtils.randomAlphabetic(3) + String.format("%02d", LocalTime.now().getSecond()) + RandomStringUtils.randomAlphabetic(3);
        return userCode;
    }

    public static int calDDay(String firstMetDay) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date firstMetDate = sdf.parse(firstMetDay);
;       Date today = new Date();
        long diff = today.getTime() - firstMetDate.getTime();
        return (int) (diff / (24*60*60*1000) + 1);
    }
}
