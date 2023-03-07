package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dao.CoupleDao;
import com.example.demo.src.data.dao.DiaryDao;
import com.example.demo.src.data.dao.UserDao;
import com.example.demo.src.data.dto.push.FcmMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.gson.JsonParseException;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.example.demo.config.BaseResponseStatus.FAIL_TO_PUSH_MESSAGE;

@Component
@Service
public class PushService {
    private final CoupleDao coupleDao;
    private final ObjectMapper objectMapper;


    @Autowired
    public PushService(CoupleDao coupleDao, ObjectMapper objectMapper){
        this.coupleDao = coupleDao;
        this.objectMapper = objectMapper;
    }

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/fromu-9ec65/messages:send";

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

            if(response.code()!=200){
                throw new BaseException(FAIL_TO_PUSH_MESSAGE);
            }
            System.out.println(response.body().string());

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
                            .notification(FcmMessage.Notification.builder()
                                    .title(title)
                                    .body(body)
                                    .image(null)
                                    .build()
                            ).build()).validateOnly(false).build();

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
