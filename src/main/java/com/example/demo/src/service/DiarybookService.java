package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dao.DiarybookDao;
import com.example.demo.src.data.dto.diarybook.*;
import com.example.demo.src.data.entity.Diarybook;
import com.example.demo.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.PATCH_COUPLES_INVALID_FIRSTMETDAY;
import static com.example.demo.utils.ValidationRegex.isRegexDay;

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
    public PostDiarybookRes createDiarybook(int userId, PostDiarybookReq postDiarybookReq) throws BaseException {
        int diarybookId = diarybookDao.createDiarybook(userId, postDiarybookReq);
        return new PostDiarybookRes(diarybookId, postDiarybookReq.getCoverNum(), postDiarybookReq.getName());
    }

    // coupleId로 일기장 조회
    public Diarybook getDiarybookByCoupleId(int coupleId) throws BaseException {
        return diarybookDao.getDiarybookByCoupleId(coupleId);
    }
    // coupleId로 일기장 조회
    public Diarybook getDiarybookByDiarybookId(int diarybookId) throws BaseException {
        return diarybookDao.getDiarybookByDiarybookId(diarybookId);
    }
    // 일기장 전체 조회
    public List<Diarybook> getDiarybooks() throws BaseException {
        return diarybookDao.getDiarybooks();
    }

    // 일기장 표지 수정
    public int modifyDiarybookCoverNum(int userId, PatchDiarybookCoverNumReq patchDiarybookReq) throws BaseException{
        return diarybookDao.modifyDiarybookCover(userId, patchDiarybookReq.getCoverNum());
    }

    public int modifyDiarybookName(int userId, PatchDiarybookNameReq patchDiarybookReq) throws BaseException{
        return diarybookDao.modifyDiarybookName(userId, patchDiarybookReq.getName());
    }

    public int setDiarybookImage(int userId, PatchDiarybookImageReq patchDiarybookReq) throws BaseException{
        return diarybookDao.setDiarybookImage(userId, patchDiarybookReq.getImageUrl());
    }
    // 일기장 삭제
}
