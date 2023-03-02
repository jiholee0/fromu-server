package com.example.demo.src.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.data.dto.couple.GetCoupleMatchRes;
import com.example.demo.src.service.CoupleService;
import com.example.demo.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/view")
public class ViewController {
    @Autowired
    private final CoupleService coupleService;
    @Autowired
    private final TokenService tokenService;

    public ViewController(CoupleService coupleService, TokenService tokenService) {
        this.coupleService = coupleService;
        this.tokenService = tokenService;
    }

    /**
     * 메인 뷰 API
     * [GET] /view/main
     * return : 닉네임, 상대방 닉네임, dday, 일기장 data
     */
}
