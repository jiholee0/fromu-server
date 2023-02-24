package com.example.demo.utils;

import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.data.dto.user.PostUserReq;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.demo.config.BaseResponseStatus.*;

public class ValidationRegex {

    public static BaseResponseStatus isRegexPostUser(PostUserReq postUserReq) {
        if(postUserReq.getEmail() == null || postUserReq.getEmail().length() == 0){
            return POST_USERS_EMPTY_EMAIL;
        }
        if(!isRegexEmail(postUserReq.getEmail())){
            return POST_USERS_INVALID_EMAIL;
        }
        if(postUserReq.getNickname() == null || postUserReq.getNickname().length() == 0){
            return POST_USERS_EMPTY_NICKNAME;
        }
        if(!isRegexNickname(postUserReq.getNickname())){
            return POST_USERS_INVALID_NICKNAME;
        }
        if(postUserReq.getBirthday() == null || postUserReq.getBirthday().length() == 0){
            return POST_USERS_EMPTY_BIRTHDAY;
        }
        if(!isRegexDay(postUserReq.getBirthday())){
            return POST_USERS_INVALID_BIRTHDAY;
        }
        if(postUserReq.getGender() == null || postUserReq.getGender().length() == 0){
            return POST_USERS_EMPTY_GENDER;
        }
        if(!isRegexGender(postUserReq.getGender())){
            return POST_USERS_INVALID_GENDER;
        }
        if(postUserReq.getFirstMetDay() == null || postUserReq.getFirstMetDay().length() == 0){
            return POST_USERS_EMPTY_FIRSTMETDAY;
        }
        if(!isRegexDay(postUserReq.getFirstMetDay())){
            return POST_USERS_INVALID_FIRSTMETDAY;
        }
        return null;
    }

    // 이메일 형식 체크
    public static boolean isRegexEmail(String target) {
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    public static boolean isRegexNickname(String nickname) {
        if (nickname.length()>5){
            return false;
        }
        return true;
    }

    public static boolean isRegexDay(String day) {
        String regex = "\\d{4}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(day);
        return matcher.find();
    }

    public static boolean isRegexGender(String gender) {
        if (Objects.equals(gender, "M") || Objects.equals(gender, "FM")){
            return true;
        }
        return false;
    }
}

