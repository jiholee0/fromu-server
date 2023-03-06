package com.example.demo.utils;

import com.example.demo.config.BaseException;
import org.apache.commons.lang3.RandomStringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

import static com.example.demo.config.BaseResponseStatus.FAIL_TO_CAL_DDAY;

public class CommonUtils {
    public static String createUserCode(){
        return RandomStringUtils.randomAlphabetic(3) + String.format("%02d", LocalTime.now().getSecond()) + RandomStringUtils.randomAlphabetic(3);
    }

    public static int calDDay(String firstMetDay) throws BaseException {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date firstMetDate = sdf.parse(firstMetDay);
            Date today = new Date();
            long diff = today.getTime() - firstMetDate.getTime();
            return (int) (diff / (24*60*60*1000) + 1);
        } catch (ParseException exception){
            throw new BaseException(FAIL_TO_CAL_DDAY);
        } catch (NullPointerException exception){
            return 0;
        }

    }
}
