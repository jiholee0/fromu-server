package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dao.CoupleDao;
import com.example.demo.src.data.dao.ShopDao;
import com.example.demo.src.data.dao.UserDao;
import com.example.demo.src.data.dto.push.FcmMessage;
import com.example.demo.src.data.entity.PushStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.JsonParseException;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@EnableScheduling
public class PushService {
    private final CoupleDao coupleDao;
    private final ShopDao shopDao;
    private final UserDao userDao;
    private final ObjectMapper objectMapper;

    private int setUserId = 0;
    private String setTitle = "";
    private String setBody = "";


    @Autowired
    public PushService(CoupleDao coupleDao, ShopDao shopDao, UserDao userDao, ObjectMapper objectMapper){
        this.coupleDao = coupleDao;
        this.shopDao = shopDao;
        this.userDao = userDao;
        this.objectMapper = objectMapper;
    }

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/fromu-9ec65/messages:send";

    public boolean sendMessageToPartner(int userId, String title, String body) throws BaseException{
        String targetToken = coupleDao.getPartnerDeviceToken(userId);
        if(shopDao.push(userId)){
            if(targetToken == null || targetToken.equals("")) return false;
            sendMessageTo(
                    targetToken,
                    title,
                    body);
            return true;
        }
        return false;
    }

    public boolean sendMessageToPartnerFree(int userId, String title, String body) throws BaseException{
        String targetToken = coupleDao.getPartnerDeviceToken(userId);
        if(targetToken == null || targetToken.equals("")) return false;
        sendMessageTo(
                targetToken,
                title,
                body);
        return true;
    }

//    @Scheduled(initialDelay = 60 * 60 * 1000) // 1시간 후에 실행
//    public boolean sendMessageToPartnerFreeAfterHour(int userId, String title, String body) throws BaseException{
//        String targetToken = coupleDao.getPartnerDeviceToken(userId);
//        if(shopDao.push(userId)){
//            if(targetToken == null || targetToken.equals("")) throw new BaseException(NOT_EXIST_DEVICE_TOKEN);
//            sendMessageTo(
//                    targetToken,
//                    title,
//                    body);
//            return true;
//        }
//        return false;
//    }

//    @Scheduled(initialDelay = 60 * 1000) // 1분 후에 실행
//    public Date testScheduled() throws BaseException{
//        return new Date();
//    }

    public void set(int userId, String title, String body){
        this.setUserId = userId;
        this.setTitle = title;
        this.setBody = body;
    }

    public boolean sendMessageFree(int userId, String title, String body) throws BaseException{
        String targetToken = userDao.getUser(userId).getDeviceToken();
        if(targetToken == null || targetToken.equals("")) return false;
        sendMessageTo(
                targetToken,
                title,
                body);
        return true;
    }

    public void sendMessageTo(String targetToken, String title, String body) throws BaseException {
        try{
            String message = makeMessage(targetToken, title, body);

            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(message,
                    MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(requestBody)
                    .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                    .build();


            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
            if(response.code()!=200){
                throw new BaseException(FAIL_TO_PUSH_MESSAGE);
            }


        } catch (IOException exception){
            exception.printStackTrace();
            throw new BaseException(FAIL_TO_PUSH_MESSAGE);
        }

    }

    private String makeMessage(String targetToken, String title, String body) throws BaseException {
        try{
            FcmMessage fcmMessage = FcmMessage.builder()
                    .message(FcmMessage.Message.builder()
                            .token(targetToken)
                            .data(FcmMessage.Data.builder()
                                    .title(title)
                                    .body(body)
                                    .build())
//                            .notification(FcmMessage.Notification.builder()
//                                    .title(title)
//                                    .body(body)
//                                    .build())
                            .build()).validateOnly(false).build();

            return objectMapper.writeValueAsString(fcmMessage);
        } catch(JsonParseException | JsonProcessingException exception){
            exception.printStackTrace();
            throw new BaseException(FAIL_TO_PUSH_MESSAGE);
        }
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase_service_key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    public String getPushMessage(int userId) throws BaseException {
        return coupleDao.getPushMessage(userId);
    }
}
