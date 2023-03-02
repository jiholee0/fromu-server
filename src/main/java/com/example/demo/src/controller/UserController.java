package com.example.demo.src.controller;

import com.example.demo.src.data.dto.user.*;
import com.example.demo.src.data.entity.User;
import com.example.demo.src.service.KakaoService;
import com.example.demo.src.service.UserService;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.utils.TokenService;
import io.swagger.annotations.ApiOperation;
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
@Tag(name = "UserController", description = "User-Controller")
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
    @Operation(method = "POST", description = "카카오 로그인 API", tags = "UserController", operationId = "0")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2003", description = "LOGIN-ACCESS-TOKEN을 입력해주세요."),
            @ApiResponse(responseCode = "3001", description = "이메일 정보를 가져오는데 실패하였습니다."),
            @ApiResponse(responseCode = "3002", description = "카카오 api 호출 응답 정보를 불러오는데 실패하였습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PostMapping("/kakao")
    public BaseResponse<PostKakaoRes> kakaoLogin() throws BaseException {
        try {
            String accessToken = tokenService.getAccessToken();
            PostKakaoRes postKakaoRes = kakaoService.getUserInfo(accessToken);
            if (postKakaoRes.isMember()) {
                postKakaoRes.getUserInfo().setJwt(tokenService.createJwt(postKakaoRes.getUserInfo().getUserId()));
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
    @Operation(method = "POST", description = "회원가입 API", tags = "UserController", operationId = "1")
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
    @Operation(method = "POST", description = "로그인 테스트 API", tags = "UserController", operationId = "3")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2010", description = "이메일을 입력해주세요."),
            @ApiResponse(responseCode = "2011", description = "이메일 형식을 확인해주세요."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<String> login(@Parameter @RequestBody String email) {
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
    @Operation(method = "GET", description = "모든 회원 조회 API", tags = "UserController", operationId = "4")
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
    @Operation(method = "GET", description = "userId로 회원 조회 API", tags = "UserController", operationId = "5")
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
     * 유저삭제 API (soft delete)
     * [PATCH] /users/:userId/d
     */
    @Operation(method = "PATCH", description = "유저 삭제 API", tags = "UserController", operationId = "6")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
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
    @Operation(method = "PATCH", description = "유저 정보 변경(닉네임, 생일) API", tags = "UserController", operationId = "7")
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
    public BaseResponse<Integer> modifyUser(@Parameter(name = "typeNum", description = "1(닉네임) / 2(생일)", required = true) @PathVariable("typeNum") int type, @RequestBody String str) {
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
