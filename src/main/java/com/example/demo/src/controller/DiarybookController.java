package com.example.demo.src.controller;


import com.example.demo.src.service.DiarybookService;
import com.example.demo.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/diarybooks")
public class DiarybookController {
    @Autowired
    private final DiarybookService diarybookService;
    @Autowired
    private final TokenService tokenService;

    public DiarybookController(DiarybookService diarybookService, TokenService tokenService) {
        this.diarybookService = diarybookService;
        this.tokenService = tokenService;
    }

    /**
     * 일기장 등록 API
     * [POST] /diarybooks
     * [jwt] -> coupleId , 표지 num, 이름 / [jwt] 로 얻은 userId = 최근 유저 id
     * return : diarybookId
     */

    /**
     * 일기장 수정 API
     * [PATCH] /diarybooks/:typeNum
     * 표지 수정 : typeNum = 1
     * 이름 수정 : typeNum = 2
     */

    /**
     * 일기장 전체 조회 API
     * [GET] /diarybooks
     */

    /**
     * couple의 일기장 조회 API
     * [GET] /diarybooks/:coupleId
     */

    /**
     * 일기장 삭제 API
     * [PATCH] /diarybooks/d
     */
}
