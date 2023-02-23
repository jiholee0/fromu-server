package com.example.demo.src.controller;

import com.example.demo.src.data.dto.user.*;
import com.example.demo.src.data.entity.User;
import com.example.demo.src.service.KakaoService;
import com.example.demo.src.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app/users")

public class UserController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final UserService userService;
    private final KakaoService kakaoService;
    @Autowired
    private final TokenService tokenService;


    public UserController(UserService userService, KakaoService kakaoService, TokenService tokenService) {
        this.userService = userService;
        this.kakaoService = kakaoService;
        this.tokenService = tokenService;
    }

    // TODO: 소셜 로그인
    /**
     * 카카오 로그인 API
     * [POST] /users/kakao
     */
    @ResponseBody
    @GetMapping("/kakao")
    public BaseResponse<PostKakaoRes> kakaoLogin() throws BaseException {
        try {
            String accessToken = tokenService.getAccessToken();
            PostKakaoRes postKakaoRes = kakaoService.getUserInfo(accessToken);
            if (!postKakaoRes.getIsMember()) {
                postKakaoRes.setJwt(tokenService.createJwt(postKakaoRes.getUserId()));
            }
            return new BaseResponse<>(postKakaoRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원가입 API
     * [POST] /users
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        try {
            PostUserRes PostRes = userService.createUser(postUserReq);
            return new BaseResponse<>(PostRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 로그인 API
     * [POST] /users/login
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<String> logIn(@RequestBody String email) {
        try {
            String jwt = userService.logIn(email);
            return new BaseResponse<>(jwt);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 모든 회원들의 조회 API
     * [GET] /users
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<User>> getUsers() {
        try {
            List<User> usersList = userService.getUsers();
            return new BaseResponse<>(usersList);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 1명 조회 API
     * [GET] /users/:userId
     */
    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<GetUserRes> getUser(@PathVariable("userId") int userId) {
        try {
            GetUserRes getUserRes = userService.getUser(userId);
            return new BaseResponse<>(getUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 유저삭제 API
     * [PATCH] /users/:userId/d
     */
    @ResponseBody
    @PatchMapping("/{userId}/d")
    public BaseResponse<Integer> deleteUser(@PathVariable("userId") int userId) {
        try {
            int userIdByJwt = tokenService.getUserId();
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            userService.deleteUser(userId);
            return new BaseResponse<>(userId);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
