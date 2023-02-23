package com.example.demo.src.service;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.data.dao.UserDao;
import com.example.demo.src.data.dto.user.*;
import com.example.demo.utils.TokenService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserDao userDao;

    @Autowired
    public KakaoService(UserDao userDao) {
        this.userDao = userDao;
    }

    // 토큰으로 카카오 API 호출
    public PostKakaoRes getUserInfo(String accessToken) throws BaseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            HttpEntity<MultiValueMap<String, String>> postLoginReq = new HttpEntity<>(headers);
            RestTemplate rt = new RestTemplate();
            ResponseEntity<String> response = rt.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.POST,
                    postLoginReq,
                    String.class
            );

            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            String email = jsonNode.get("kakao_account").get("email").asText();

            if (userDao.checkEmail(email)) {
                return new PostKakaoRes(true, email, userDao.getUserIdByEmail(email), null);
            } else {
                return new PostKakaoRes(false, email, null, null);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
