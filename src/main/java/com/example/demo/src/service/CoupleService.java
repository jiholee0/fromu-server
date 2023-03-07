package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dao.CoupleDao;
import com.example.demo.src.data.dao.UserDao;
import com.example.demo.src.data.dto.couple.CoupleRes;
import com.example.demo.src.data.dto.couple.GetCoupleMatchRes;
import com.example.demo.src.data.dto.couple.PostCoupleReq;
import com.example.demo.src.data.entity.Couple;
import com.example.demo.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexDay;
import static com.example.demo.utils.ValidationRegex.isRegexNickname;

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
    public CoupleRes createCouple(int userId, PostCoupleReq postCoupleReq) throws BaseException {
        return coupleDao.createCouple(userId, postCoupleReq);
    }

    // Couple 전체 조회
    public List<Couple> getCouples() throws BaseException {
        return coupleDao.getCouples();
    }

    // userId로 Couple 조회
    public Couple getCoupleByUserId(int userId) throws BaseException {
        return coupleDao.getCoupleByUserId(userId);
    }

    // coupleId로 Couple 조회
    public Couple getCoupleByCoupleId(int coupleId) throws BaseException {
        return coupleDao.getCoupleByCoupleId(coupleId);
    }

    // 매칭 여부 확인(새로고침)
    public GetCoupleMatchRes getCoupleMatch(int userId) throws BaseException {
        return coupleDao.getCoupleMatchRes(userId);
    }

    public int modifyFirstMetDay(int userId, String str) throws BaseException{
        if (!isRegexDay(str)){
            throw new BaseException(PATCH_COUPLES_INVALID_FIRSTMETDAY);
        }
        return coupleDao.modifyFirstMetDay(userId, str);
    }

    public int modifyMailbox(int userId, String str) throws BaseException{
        if (!isRegexNickname(str)){
            throw new BaseException(PATCH_COUPLES_INVALID_MAILBOX);
        }
        return coupleDao.modifyMailbox(userId, str);
    }

    public int modifyPushMessage(int userId, String str) throws BaseException{
        return coupleDao.modifyPushMessage(userId, str);
    }

    public int deleteCouple(int userId) throws BaseException {
        return coupleDao.deleteCouple(userId);
    }

    public boolean checkMailbox(String mailbox) throws BaseException {
        if (!isRegexNickname(mailbox)){
            throw new BaseException(PATCH_COUPLES_INVALID_MAILBOX);
        }
        return coupleDao.checkMailbox(mailbox);
    }
}
