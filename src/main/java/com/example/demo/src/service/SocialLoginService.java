package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dao.CoupleDao;
import com.example.demo.src.data.dao.UserDao;
import com.example.demo.src.data.dto.user.PostSocialLoginRes;
import com.example.demo.src.data.dto.user.UserInfo;
import com.example.demo.src.data.entity.User;
import com.example.demo.utils.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Objects;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.config.BaseResponseStatus.FAIL_TO_FIND_AVALIABLE_RSA;

@Service
public class SocialLoginService {
    private final UserDao userDao;
    private final CoupleDao coupleDao;
    private final TokenService tokenService;

    @Autowired
    public SocialLoginService(UserDao userDao, CoupleDao coupleDao, TokenService tokenService) {
        this.userDao = userDao;
        this.coupleDao = coupleDao;
        this.tokenService = tokenService;
    }

    public PostSocialLoginRes getUserInfoByKakao(String accessToken) throws BaseException {
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

            if (email == null || email.equals("")) {
                throw new BaseException(FAIL_TO_GET_EMAIL);
            }
            if (userDao.checkEmail(email)) {
                User user = userDao.getUserByEmail(email);
                String refreshToken = tokenService.createRefreshToken(user.getUserId());
                UserInfo userInfo = new UserInfo(
                        coupleDao.checkUserId(user.getUserId()),
                        coupleDao.isSetMailboxName(user.getUserId()),
                        user.getUserId(),
                        null,
                        refreshToken,
                        email,
                        user.getUserCode()
                );
                userDao.saveRefreshToken(user.getUserId(), refreshToken);
                return new PostSocialLoginRes(true, userInfo);
            } else {
                return new PostSocialLoginRes(false, null);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BaseException(FAIL_TO_RESPONSE_KAKAO);
        }
    }

    public PostSocialLoginRes getUserInfoByApple(String idToken) throws BaseException {
        // 공개키 받아오기
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL("https://appleid.apple.com/auth/keys");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            throw new BaseException(FAIL_TO_RESPONSE_APPLE);
        }

        JsonParser parser = new JsonParser();
        JsonObject keys = (JsonObject) parser.parse(result.toString());
        JsonArray keyArray = (JsonArray) keys.get("keys");


        //클라이언트로부터 가져온 identity token String decode
        String[] decodeArray = idToken.split("\\.");
        String header = new String(Base64.getDecoder().decode(decodeArray[0]));

        //apple에서 제공해주는 kid값과 일치하는지 알기 위해
        JsonElement kid = ((JsonObject) parser.parse(header)).get("kid");
        JsonElement alg = ((JsonObject) parser.parse(header)).get("alg");

        //써야하는 Element (kid, alg 일치하는 element)
        JsonObject avaliableObject = null;
        for (int i = 0; i < keyArray.size(); i++) {
            JsonObject appleObject = (JsonObject) keyArray.get(i);
            JsonElement appleKid = appleObject.get("kid");
            JsonElement appleAlg = appleObject.get("alg");

            if (Objects.equals(appleKid, kid) && Objects.equals(appleAlg, alg)) {
                avaliableObject = appleObject;
                break;
            }
        }

        //일치하는 공개키 없음
        if (ObjectUtils.isEmpty(avaliableObject))
            throw new BaseException(FAIL_TO_FIND_AVALIABLE_RSA);

        PublicKey publicKey = this.getPublicKey(avaliableObject);

        Claims claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(idToken).getBody();
        JsonObject userInfoObject = (JsonObject) JsonParser.parseString(new Gson().toJson(claims));
        JsonElement appleAlg = userInfoObject.get("email");
        String email = appleAlg.getAsString();


        if (email == null || email.equals("")) {
            throw new BaseException(FAIL_TO_GET_EMAIL);
        }
        if (userDao.checkEmail(email)) {
            User user = userDao.getUserByEmail(email);
            String refreshToken = tokenService.createRefreshToken(user.getUserId());
            UserInfo userInfo = new UserInfo(
                    coupleDao.checkUserId(user.getUserId()),
                    coupleDao.isSetMailboxName(user.getUserId()),
                    user.getUserId(),
                    null,
                    refreshToken,
                    email,
                    user.getUserCode()
            );
            userDao.saveRefreshToken(user.getUserId(), refreshToken);
            return new PostSocialLoginRes(true, userInfo);
        } else {
            return new PostSocialLoginRes(false, null);
        }
    }

    public PublicKey getPublicKey(JsonObject object) throws BaseException {
        String nStr = object.get("n").toString();
        String eStr = object.get("e").toString();

        byte[] nBytes = Base64.getUrlDecoder().decode(nStr.substring(1, nStr.length() - 1));
        byte[] eBytes = Base64.getUrlDecoder().decode(eStr.substring(1, eStr.length() - 1));

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            return publicKey;
        } catch (Exception exception) {
            throw new BaseException(FAIL_TO_FIND_AVALIABLE_RSA);
        }
    }

    public PostSocialLoginRes getUserInfoByGoogle(String accessToken) throws BaseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            HttpEntity<MultiValueMap<String, String>> postLoginReq = new HttpEntity<>(headers);
            RestTemplate rt = new RestTemplate();
            ResponseEntity<String> response = rt.exchange(
                    "https://www.googleapis.com/oauth2/v2/userinfo",
                    HttpMethod.POST,
                    postLoginReq,
                    String.class
            );
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            String email = jsonNode.get("email").asText();

            if (email == null || email.equals("")) {
                throw new BaseException(FAIL_TO_GET_EMAIL);
            }
            if (userDao.checkEmail(email)) {
                User user = userDao.getUserByEmail(email);
                String refreshToken = tokenService.createRefreshToken(user.getUserId());
                UserInfo userInfo = new UserInfo(
                        coupleDao.checkUserId(user.getUserId()),
                        coupleDao.isSetMailboxName(user.getUserId()),
                        user.getUserId(),
                        null,
                        refreshToken,
                        email,
                        user.getUserCode()
                );
                userDao.saveRefreshToken(user.getUserId(), refreshToken);
                return new PostSocialLoginRes(true, userInfo);
            } else {
                return new PostSocialLoginRes(false, null);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BaseException(FAIL_TO_RESPONSE_GOOGLE);
        }
    }
}
