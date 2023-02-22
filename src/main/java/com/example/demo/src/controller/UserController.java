package com.example.demo.src.controller;

import com.example.demo.src.data.dto.user.*;
import com.example.demo.src.service.KakaoService;
import com.example.demo.src.service.UserService;
import com.example.demo.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/users")

public class UserController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserService userService;
    private final KakaoService kakaoService;
    @Autowired
    private final JwtService jwtService;
    private final CommonUtils commonUtils;


    public UserController(UserService userService, KakaoService kakaoService, JwtService jwtService, CommonUtils commonUtils) {
        this.userService = userService;
        this.kakaoService = kakaoService;
        this.jwtService = jwtService;
        this.commonUtils = commonUtils;
    }

    // TODO: 소셜 로그인
    /**
     * 카카오 로그인 API
     * [POST] /users/kakao
     */
    @ResponseBody
    @GetMapping("/kakao")
    public BaseResponse<PostKakaoRes> kakaoLogin(@RequestParam String accessToken) throws BaseException {
        try {
            PostKakaoRes postKakaoRes = kakaoService.getUserInfo(accessToken);
            if (!postKakaoRes.getIsMember()) {
                postKakaoRes.setJwt(jwtService.createJwt(postKakaoRes.getUserId()));
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
     * 모든 회원들의 조회 API
     * [GET] /users
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetUserRes>> getUsers() {
        try {
            List<GetUserRes> getUsersRes = userService.getUsers();
            return new BaseResponse<>(getUsersRes);
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
            int userIdByJwt = jwtService.getUserId();
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
