package com.example.demo.src.controller;

import com.example.demo.src.data.dto.user.*;
import com.example.demo.src.data.entity.User;
import com.example.demo.src.service.KakaoService;
import com.example.demo.src.service.UserService;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/users")

public class UserController {
    @Autowired
    private final UserService userService;
    @Autowired
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
    @PostMapping("/kakao")
    public BaseResponse<PostKakaoRes> kakaoLogin() throws BaseException {
        try {
            String accessToken = tokenService.getAccessToken();
            PostKakaoRes postKakaoRes = kakaoService.getUserInfo(accessToken);
            if (postKakaoRes.getIsMember()) {
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
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
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
    public BaseResponse<String> login(@RequestBody String email) {
        try {
            String jwt = userService.login(email);
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
    public BaseResponse<User> getUser(@PathVariable("userId") int userId) {
        try {
            User user = userService.getUser(userId);
            return new BaseResponse<>(user);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 유저삭제 API
     * [PATCH] /users/:userId/d
     */
    @ResponseBody
    @PatchMapping("/d")
    public BaseResponse<Integer> deleteUser() {
        try {
            int userIdByJwt = tokenService.getUserId();
            userService.deleteUser(userIdByJwt);
            return new BaseResponse<>(userIdByJwt);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 정보 변경 API
     * [PATCH] /users/:typeNum
     * 닉네임 수정 : typeNum = 1
     * 생일 수정 : typeNum = 2
     */
    @ResponseBody
    @PatchMapping("/{typeNum}")
    public BaseResponse<Integer> modifyUser(@PathVariable("typeNum") int type, @RequestBody String str) {
        try {
            int userIdByJwt = tokenService.getUserId();
            if (type == 1 || type == 2) {
                userService.modifyUser(userIdByJwt, type, str);
                return new BaseResponse<>(userIdByJwt);
            } else {
                throw new BaseException(INVALID_REQ_PARAM);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
