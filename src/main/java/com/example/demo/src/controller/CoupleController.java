package com.example.demo.src.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.data.dto.couple.CoupleRes;
import com.example.demo.src.data.dto.couple.GetCoupleMatchRes;
import com.example.demo.src.data.dto.couple.PatchCoupleRes;
import com.example.demo.src.data.entity.Couple;
import com.example.demo.src.service.CoupleService;
import com.example.demo.utils.TokenService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

import static com.example.demo.utils.CommonUtils.*;
import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@Tag(name = "CoupleController")
@RequestMapping("/app/couples")
public class CoupleController {
    @Autowired
    private final CoupleService coupleService;
    @Autowired
    private final TokenService tokenService;

    public CoupleController(CoupleService coupleService, TokenService tokenService) {
        this.coupleService = coupleService;
        this.tokenService = tokenService;
    }

    /**
     * 커플 생성 API
     * [POST] /couples
     * return : coupleId, 본인 닉네임, 상대방 닉네임
     */
    @Operation(method = "POST", description = "커플 생성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2020", description = "이미 커플 매칭이 완료된 유저입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PostMapping("")
    public BaseResponse<CoupleRes> createCouple(@RequestBody String partnerCode) {
        try {
            int userIdByJwt = tokenService.getUserId();
            CoupleRes coupleRes = coupleService.createCouple(userIdByJwt, partnerCode);
            return new BaseResponse<>(coupleRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 만난 날 설정 및 수정 API
     * [PATCH] /couples/firstMetDay
     */
    @Operation(method = "PATCH", description = "만난 날 설정 및 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2060", description = "만난 날 형식을 확인해주세요."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PatchMapping("/firstMetDay")
    public BaseResponse<PatchCoupleRes> modifyFirstMetDay(@RequestBody String str) {
        try {
            int userIdByJwt = tokenService.getUserId();
            coupleService.modifyFirstMetDay(userIdByJwt, str);
            return new BaseResponse<>(new PatchCoupleRes(userIdByJwt, str));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 우편함 이름 설정 및 수정 API
     * [PATCH] /couples/mailbox
     */
    @Operation(method = "PATCH", description = "우편함 이름 설정 및 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2061", description = "우편함 이름 형식을 확인해주세요."),
            @ApiResponse(responseCode = "2062", description = "중복된 우편함 이름입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PatchMapping("/mailbox")
    public BaseResponse<PatchCoupleRes> modifyMailbox(@RequestBody String str) {
        try{
            int userIdByJwt = tokenService.getUserId();
            coupleService.modifyMailbox(userIdByJwt, str);
            return new BaseResponse<>(new PatchCoupleRes(userIdByJwt, str));
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 커플 매칭 끊기 API
     * [PATCH] /couples/d
     */
    @Operation(method = "PATCH", description = "커플 연결 끊기 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PatchMapping("/d")
    public BaseResponse<Integer> deleteCouple() {
        try {
            int userIdByJwt = tokenService.getUserId();
            coupleService.deleteCouple(userIdByJwt);
            return new BaseResponse<>(userIdByJwt);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 커플 전체 조회 API
     * [GET] /couples
     */
    @Operation(method = "GET", description = "커플 전체 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.")
    })
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<Couple>> getCouples() {
        try {
            List<Couple> couplesList = coupleService.getCouples();
            return new BaseResponse<>(couplesList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 커플 id로 커플 조회 API
     * [GET] /couples/:coupleId
     */
    @Operation(method = "GET", description = "coupleId로 커플 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @GetMapping("/{coupleId}")
    public BaseResponse<Couple> getCoupleByCoupleID(@PathVariable("coupleId") int coupleId){
        try {
            Couple couple = coupleService.getCoupleByCoupleId(coupleId);
            return new BaseResponse<>(couple);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저 id로 커플 조회 API
     * [GET] /couples/users/:userId
     */
    @Operation(method = "GET", description = "userId로 커플 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @GetMapping("/users/{userId}")
    public BaseResponse<Couple> getCoupleByUserId(@PathVariable("userId") int userId){
        try {
            Couple couple = coupleService.getCoupleByUserId(userId);
            return new BaseResponse<>(couple);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 매칭 여부 확인(새로고침) API
     * [GET] /couples/isMatch
     * return : 매칭 여부 / coupleId, 본인 닉네임, 상대방 닉네임
     */
    @Operation(method = "PATCH", description = "매칭 여부 확인(새로고침) API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.")
    })
    @ResponseBody
    @GetMapping("/isMatch")
    public BaseResponse<GetCoupleMatchRes> getCoupleMatch(){
        try {
            int userIdByJwt = tokenService.getUserId();
            GetCoupleMatchRes getCoupleMatchRes = coupleService.getCoupleMatch(userIdByJwt);
            return new BaseResponse<>(getCoupleMatchRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
