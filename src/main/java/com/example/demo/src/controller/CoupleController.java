package com.example.demo.src.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.data.dto.couple.CoupleRes;
import com.example.demo.src.data.dto.couple.GetCoupleMatchRes;
import com.example.demo.src.data.dto.couple.PatchCoupleRes;
import com.example.demo.src.data.entity.Couple;
import com.example.demo.src.service.CoupleService;
import com.example.demo.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

import static com.example.demo.utils.CommonUtils.*;
import static com.example.demo.config.BaseResponseStatus.*;

@RestController
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
    @ResponseBody
    @PatchMapping("/firstMetDay")
    public BaseResponse<PatchCoupleRes> modifyFirstMetDay(@RequestBody String str) {
        try {
            int userIdByJwt = tokenService.getUserId();
            coupleService.modifyFirstMetDay(userIdByJwt, str);
            int dDay = calDDay(str);
            return new BaseResponse<>(new PatchCoupleRes(userIdByJwt, dDay));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 커플 매칭 끊기 API
     * [PATCH] /couples/d
     */
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
