package com.example.demo.src.service;


import com.example.demo.config.BaseException;
import com.example.demo.src.data.dto.user.*;
import com.example.demo.src.data.dao.UserDao;
import com.example.demo.utils.CommonUtils;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserDao userDao;
    private final JwtService jwtService;
    private final CommonUtils commonUtils;


    @Autowired
    public UserService(UserDao userDao, JwtService jwtService, CommonUtils commonUtils) {
        this.userDao = userDao;
        this.jwtService = jwtService;
        this.commonUtils = commonUtils;
    }

    // 회원가입(POST)
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        String userCode = commonUtils.createUserCode();
        int userId = userDao.createUser(postUserReq, userCode);
        try {
            String jwt = jwtService.createJwt(userId);
            return new PostUserRes(userId, jwt, userCode);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // User 전체 조회
    public List<GetUserRes> getUsers() throws BaseException {
        try {
            List<GetUserRes> getUserRes = userDao.getUsers();
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // userId로 User 조회
    public GetUserRes getUser(int userId) throws BaseException {
        try {
            GetUserRes getUserRes = userDao.getUser(userId);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // User 삭제
    public void deleteUser(int userId) throws BaseException {
        try {
            userDao.deleteUser(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
