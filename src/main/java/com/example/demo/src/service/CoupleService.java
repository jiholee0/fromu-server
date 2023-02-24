package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dao.CoupleDao;
import com.example.demo.src.data.dao.UserDao;
import com.example.demo.src.data.dto.couple.PostCoupleReq;
import com.example.demo.src.data.dto.couple.PostCoupleRes;
import com.example.demo.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

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
    public PostCoupleRes createCouple(PostCoupleReq postCoupleReq) throws BaseException {
        try {
            int user2Id = userDao.getUserIdByUserCode(postCoupleReq.getUser2Code());
            if (coupleDao.checkUserId(postCoupleReq.getUser1Id(),user2Id)){
                throw new BaseException(POST_COUPLES_EXISTS_USER);
            }
            return coupleDao.createCouple(postCoupleReq, user2Id);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }


    }
}
