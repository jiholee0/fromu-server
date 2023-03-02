package com.example.demo.src.service;


import com.example.demo.config.BaseException;
import com.example.demo.src.data.dao.CoupleDao;
import com.example.demo.src.data.dao.UserDao;
import com.example.demo.src.data.dto.user.*;

import com.example.demo.src.data.entity.User;
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

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class KakaoService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserDao userDao;
    private final CoupleDao coupleDao;

    @Autowired
    public KakaoService(UserDao userDao, CoupleDao coupleDao) {
        this.userDao = userDao;
        this.coupleDao = coupleDao;
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

            if (email == null) {
                throw new BaseException(FAIL_TO_GET_EMAIL);
            }
            if (userDao.checkEmail(email)) {
                User user = userDao.getUserByEmail(email);
                UserInfo userInfo = new UserInfo(
                        coupleDao.checkUserId(user.getUserId()),
                                coupleDao.isSetMailboxName(user.getUserId()),
                                user.getUserId(),
                                null,
                                email,
                                user.getUserCode()
                        );
                return new PostKakaoRes(true, userInfo);
            } else {
                return new PostKakaoRes(false, null);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BaseException(FAIL_TO_RESPONSE_KAKAO);
        }
    }

}
