package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dao.CoupleDao;
import com.example.demo.src.data.dao.UserDao;
import com.example.demo.src.data.dto.couple.CoupleRes;
import com.example.demo.src.data.dto.couple.GetCoupleMatchRes;
import com.example.demo.src.data.dto.couple.PostCoupleReq;
import com.example.demo.src.data.entity.Couple;
import com.example.demo.src.data.entity.User;
import com.example.demo.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexDay;

@Service
public class CoupleService {
    private final CoupleDao coupleDao;
    private final UserDao userDao;
    private final TokenService tokenService;

    @Autowired
    public CoupleService(CoupleDao coupleDao, UserDao userDao, TokenService tokenService) {
        this.coupleDao = coupleDao;
        this.userDao = userDao;
        this.tokenService = tokenService;
    }

    // 커플 등록(POST)
    public CoupleRes createCouple(int userId, String partnerCode) throws BaseException {
        try {
            return coupleDao.createCouple(userId, partnerCode);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // Couple 전체 조회
    public List<Couple> getCouples() throws BaseException {
        try {
            return coupleDao.getCouples();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // userId로 Couple 조회
    public Couple getCoupleByUserId(int userId) throws BaseException {
        try {
            return coupleDao.getCoupleByUserId(userId);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // coupleId로 Couple 조회
    public Couple getCoupleByCoupleId(int coupleId) throws BaseException {
        try {
            return coupleDao.getCoupleByCoupleId(coupleId);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 매칭 여부 확인(새로고침)
    public GetCoupleMatchRes getCoupleMatch(int userId) throws BaseException {
        try {
            return coupleDao.getCoupleMatchRes(userId);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyFirstMetDay(int userId, String str) throws BaseException{
        if (!isRegexDay(str)){
            throw new BaseException(PATCH_COUPLES_INVALID_FIRSTMETDAY);
        }
        try {
            coupleDao.modifyFirstMetDay(userId, str);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteCouple(int userId) throws BaseException {
        try {
            coupleDao.deleteCouple(userId);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
