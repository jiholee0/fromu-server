package com.example.demo.src.controller;

import com.example.demo.src.data.dto.user.*;
import com.example.demo.src.data.entity.User;
import com.example.demo.src.service.SocialLoginService;
import com.example.demo.src.service.UserService;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.utils.TokenService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/users")
@Tag(name = "USER", description = "유저 등록/조회/수정/삭제 API")
public class UserController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final SocialLoginService socialLoginService;
    @Autowired
    private final TokenService tokenService;


    public UserController(UserService userService, SocialLoginService socialLoginService, TokenService tokenService) {
        this.userService = userService;
        this.socialLoginService = socialLoginService;
        this.tokenService = tokenService;
    }

    /**
     * 카카오 로그인 API
     * [POST] /users/kakao
     */
    @Operation(method = "POST",
            description = "Header-'X-ACCESS-TOKEN'에 소셜 로그인 accessToken 값을 넣어 " +
                    "(멤버 여부, 매칭 여부, 우편함 이름 설정 여부, 유저 ID, JWT, RefreshToken, 이메일, 유저 코드) 를 반환받는 api이며, " +
                    "JWT와 RefreshToken을 새로 발급합니다.",
            tags = "USER", summary = "카카오 로그인 API - \uD83D\uDD12 소셜 로그인 accessToken")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2003", description = "accessToken을 입력해주세요."),
            @ApiResponse(responseCode = "3001", description = "이메일 정보를 가져오는데 실패하였습니다."),
            @ApiResponse(responseCode = "3002", description = "카카오 api 호출 응답 정보를 불러오는데 실패하였습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PostMapping("/kakao")
    public BaseResponse<PostSocialLoginRes> kakaoLogin() throws BaseException {
        try {
            String accessToken = tokenService.getAccessToken();
            PostSocialLoginRes postKakaoRes = socialLoginService.getUserInfoByKakao(accessToken);
            if (postKakaoRes.isMember()) {
                postKakaoRes.getUserInfo().setJwt(tokenService.createJwt(postKakaoRes.getUserInfo().getUserId()));
            }
            return new BaseResponse<>(postKakaoRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 애플 로그인 API
     * [POST] /users/apple
     */
    @Operation(method = "POST",
            description = "Header-'X-ACCESS-TOKEN'에 소셜 로그인 accessToken 값을 넣어 " +
                    "(멤버 여부, 매칭 여부, 우편함 이름 설정 여부, 유저 ID, JWT, RefreshToken, 이메일, 유저 코드) 를 반환받는 api이며, " +
                    "JWT와 RefreshToken을 새로 발급합니다.",
            tags = "USER", summary = "애플 로그인 API - \uD83D\uDD12 소셜 로그인 accessToken")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2003", description = "accessToken을 입력해주세요."),
            @ApiResponse(responseCode = "3001", description = "이메일 정보를 가져오는데 실패하였습니다."),
            @ApiResponse(responseCode = "3003", description = "애플 api 호출 응답 정보를 불러오는데 실패하였습니다."),
            @ApiResponse(responseCode = "3004", description = "사용 가능한 공개키가 없습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PostMapping("/apple")
    public BaseResponse<PostSocialLoginRes> appleLogin() throws BaseException {
        try {
            String accessToken = tokenService.getAccessToken();
            PostSocialLoginRes postSocialLoginRes = socialLoginService.getUserInfoByApple(accessToken);
            if (postSocialLoginRes.isMember()) {
                postSocialLoginRes.getUserInfo().setJwt(tokenService.createJwt(postSocialLoginRes.getUserInfo().getUserId()));
            }
            return new BaseResponse<>(postSocialLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 구글 로그인 API
     * [POST] /users/google
     */
    @Operation(method = "POST",
            description = "Header-'X-ACCESS-TOKEN'에 소셜 로그인 accessToken 값을 넣어 " +
                    "(멤버 여부, 매칭 여부, 우편함 이름 설정 여부, 유저 ID, JWT, RefreshToken, 이메일, 유저 코드) 를 반환받는 api이며, " +
                    "JWT와 RefreshToken을 새로 발급합니다.",
            tags = "USER", summary = "구글 로그인 API - \uD83D\uDD12 소셜 로그인 accessToken")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2003", description = "accessToken을 입력해주세요."),
            @ApiResponse(responseCode = "3001", description = "이메일 정보를 가져오는데 실패하였습니다."),
            @ApiResponse(responseCode = "3005", description = "구글 api 호출 응답 정보를 불러오는데 실패하였습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PostMapping("/google")
    public BaseResponse<PostSocialLoginRes> googleLogin() throws BaseException {
        try {
            String accessToken = tokenService.getAccessToken();
            PostSocialLoginRes postKakaoRes = socialLoginService.getUserInfoByGoogle(accessToken);
            if (postKakaoRes.isMember()) {
                postKakaoRes.getUserInfo().setJwt(tokenService.createJwt(postKakaoRes.getUserInfo().getUserId()));
            }
            return new BaseResponse<>(postKakaoRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * Refresh Token API
     * [POST] /users/refreshToken
     */
    @Operation(method = "POST",
            description = "Header-'X-ACCESS-TOKEN'에 refreshToken 값을 넣어 " +
                    "(매칭 여부, 우편함 이름 설정 여부, 유저 ID, JWT, RefreshToken, 이메일, 유저 코드) 를 반환받는 api이며, " +
                    "JWT와 RefreshToken을 새로 발급합니다.",
            tags = "USER", summary = "Refresh Token 로그인 API - \uD83D\uDD12 refreshToken")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PostMapping("/refreshToken")
    public BaseResponse<UserInfo> loginRefreshToken() throws BaseException {
        try {
            String accessToken = tokenService.getJwt();
            UserInfo userInfo = userService.loginRefreshToken(accessToken);
            return new BaseResponse<>(userInfo);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * Device Token 저장 API
     * [PATCH] /users/deviceToken
     */
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣어 " +
                    "해당 유저 데이터에 device token 정보를 저장하는 api입니다.",
            tags = "USER", summary = "Device Token 저장 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PatchMapping("/deviceToken")
    public BaseResponse<PatchUserRes> saveDeviceToken(@Parameter @RequestBody PatchDeviceTokenReq patchDeviceTokenReq) throws BaseException {
        try {
            int userIdByJwt= tokenService.getUserId();
            PatchUserRes patchUserRes = userService.saveDeviceToken(userIdByJwt, patchDeviceTokenReq.getDeviceToken());
            return new BaseResponse<>(patchUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원가입 API
     * [POST] /users
     */
    @Operation(method = "POST",
            description = "유저 정보(이메일, 닉네임, 생일, 성별)를 입력하여 " +
            "(유저 ID, 코드, JWT, RefreshToken) 을 발급받는 api입니다.", tags = "USER", summary = "회원가입 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2010", description = "이메일을 입력해주세요."),
            @ApiResponse(responseCode = "2011", description = "이메일 형식을 확인해주세요."),
            @ApiResponse(responseCode = "2012", description = "이미 가입한 이메일입니다."),
            @ApiResponse(responseCode = "2013", description = "닉네임을 입력해주세요."),
            @ApiResponse(responseCode = "2014", description = "닉네임 형식을 확인해주세요."),
            @ApiResponse(responseCode = "2015", description = "생일을 입력해주세요."),
            @ApiResponse(responseCode = "2016", description = "생일 형식을 확인해주세요."),
            @ApiResponse(responseCode = "2017", description = "성별을 입력해주세요."),
            @ApiResponse(responseCode = "2018", description = "성별 형식을 확인해주세요."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.")
    })
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@Parameter @RequestBody PostUserReq postUserReq) {
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
    @Operation(method = "POST",
            description = "테스트용 api로, 이메일로 " +
            "(매칭 여부, 우편함 이름 설정 여부, 유저 ID, JWT, RefreshToken, 이메일, 유저 코드) 를 반환받는 api이며, " +
            "JWT를 새로 발급합니다.", tags = "USER", summary = "로그인 테스트 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2010", description = "이메일을 입력해주세요."),
            @ApiResponse(responseCode = "2011", description = "이메일 형식을 확인해주세요."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<UserInfo> login(@Parameter @RequestBody String email) {
        try {
            UserInfo userInfo = userService.login(email);
            return new BaseResponse<>(userInfo);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 모든 회원들의 조회 API
     * [GET] /users
     */
    @Operation(method = "GET",
            description = "유저 객체 그대로 반환하는 api입니다.", tags = "USER", summary = "모든 회원 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.")
    })
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
    @Operation(method = "GET",
            description = "유저 객체 그대로 반환하는 api입니다.", tags = "USER", summary = "userId로 회원 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
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
     * [PATCH] /users/d
     */
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣어 해당 유저를 탈퇴시키는 api입니다.(soft delete)",
            tags = "USER", summary = "탈퇴하기 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PatchMapping("/d")
    public BaseResponse<PatchUserRes> deleteUser() {
        try {
            int userIdByJwt = tokenService.getUserId();
            userService.deleteUser(userIdByJwt);
            return new BaseResponse<>(new PatchUserRes(userIdByJwt));
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
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣어 " +
            "경로 변수 typeNum에 따라 유저의 닉네임 또는 생일을 변경하는 api입니다.",
            tags = "USER", summary = "유저 정보 변경(닉네임, 생일) API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2004", description = "파라미터 값을 확인해주세요."),
            @ApiResponse(responseCode = "2050", description = "닉네임 형식을 확인해주세요."),
            @ApiResponse(responseCode = "2051", description = "생일 형식을 확인해주세요."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PatchMapping("/{typeNum}")
    public BaseResponse<PatchUserRes> modifyUser(@Parameter(name = "typeNum", description = "1(닉네임) / 2(생일)", required = true) @PathVariable("typeNum") int type, @RequestBody PatchUserReq patchUserReq) {
        try {
            int userIdByJwt = tokenService.getUserId();
            if (type == 1 || type == 2) {
                userService.modifyUser(userIdByJwt, type, patchUserReq.getString());
                return new BaseResponse<>(new PatchUserRes(userIdByJwt));
            } else {
                throw new BaseException(INVALID_REQ_PARAM);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 로그아웃 API
     * [PATCH] /users/logout
     */
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣어 로그아웃 처리하는 api입니다.",
            tags = "USER", summary = "로그아웃 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PatchMapping("/logout")
    public BaseResponse<PatchUserRes> logout() {
        try {
            int userIdByJwt = tokenService.getUserId();
            userService.logout(userIdByJwt);
            return new BaseResponse<>(new PatchUserRes(userIdByJwt));
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
