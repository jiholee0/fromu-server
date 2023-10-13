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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
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
public class PushService {
    private final CoupleDao coupleDao;
    private final ShopDao shopDao;
    private final UserDao userDao;
    private final ObjectMapper objectMapper;

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

    public void sendMessageToWithIOS(String targetToken, String title, String body) throws Exception {
        Message message = Message.builder()
                .setToken(targetToken)
                .setNotification(new Notification(title, body))
                .build();
        String response = FirebaseMessaging.getInstance().send(message);
        System.out.println("Successfully sent message: " + response);
    }

    public void sendMessageToPartnerFree(int userId, String title, String body) throws BaseException{
        String targetToken = coupleDao.getPartnerDeviceToken(userId);
        if(targetToken == null || targetToken.equals("")) return;
        sendMessageTo(
                targetToken,
                title,
                body);
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
        Response response;
        try{
            String message = makeMessage(targetToken, title, body);
            if(message == null) return;
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(message,
                    MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(requestBody)
                    .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                    .build();
            response = client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException exception){
            exception.printStackTrace();
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
                            .notification(FcmMessage.Notification.builder()
                                    .title(title)
                                    .body(body)
                                    .build())
                            .build()).validateOnly(false).build();

            return objectMapper.writeValueAsString(fcmMessage);
        } catch(JsonParseException | JsonProcessingException exception){
            exception.printStackTrace();
            return null;
        }
    }

    private String getAccessToken() throws IOException {
        //String firebaseConfigPath = "firebase_service_key.json";
        String firebaseConfigPath = "fromu-9ec65-firebase-adminsdk-gndzq-d943f1c0be.json";

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
