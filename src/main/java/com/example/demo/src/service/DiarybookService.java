package com.example.demo.src.service;

import com.example.demo.src.data.dao.DiarybookDao;
import com.example.demo.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiarybookService {
    private final DiarybookDao diarybookDao;
    private final TokenService tokenService;

    @Autowired
    public DiarybookService(DiarybookDao diarybookDao, TokenService tokenService) {
        this.diarybookDao = diarybookDao;
        this.tokenService = tokenService;
    }

    // 일기장 등록

    // coupleId로 일기장 조회
    // 일기장 전체 조회

    // 일기장 수정
    // 일기장 삭제
}
