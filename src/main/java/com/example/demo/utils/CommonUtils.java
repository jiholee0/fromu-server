package com.example.demo.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalTime;


public class CommonUtils {
    public static String createUserCode(){
        String userCode = RandomStringUtils.randomAlphabetic(3) + String.format("%02d", LocalTime.now().getSecond()) + RandomStringUtils.randomAlphabetic(3);
        return userCode;
    }
}
