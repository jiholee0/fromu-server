package com.example.demo.src.controller;

import com.example.demo.src.service.CoupleService;
import com.example.demo.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/couples")

public class CoupleController {
//    @Autowired
//    private final CoupleService coupleService;
//    @Autowired
//    private final TokenService tokenService;
//
//    public CoupleController(CoupleService coupleService, TokenService tokenService) {
//        this.coupleService = coupleService;
//        this.tokenService = tokenService;
//    }

    /**
     * 커플 생성 API
     * [POST] /couples
     */

    /**
     * 커플 전체 조회 API
     * [GET] /couples
     */

    /**
     * 유저 id로 커플 조회 API
     * [GET] /couples/:userId
     */

    /**
     * 매칭 여부 확인 API
     * [GET] /couples/:userId/isMatch
     */

    /**
     * 우편함 이름 수정 API
     * [PATCH] /couples/:userId/mailbox
     */

    /**
     * 커플 매칭 끊기 API
     * [PATCH] /couples/:coupleId/d
     */

    /**
     * 쿡 찌르기 사용 API
     * [PATCH] /couples/:userId/sting
     * 남은 횟수 return
     */

    /**
     * 쿡 찌르기 충전 API
     * [PATCH] /couples/:userId/sting?count={count}
     */

}
