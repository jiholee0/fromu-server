package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.data.dto.user.*;
import com.example.demo.src.data.dao.UserDao;
import com.example.demo.src.data.entity.User;
import com.example.demo.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.CommonUtils.*;
import static com.example.demo.utils.ValidationRegex.*;

@Service
public class UserService {
    private final UserDao userDao;
    private final TokenService tokenService;

    @Autowired
    public UserService(UserDao userDao, TokenService tokenService) {
        this.userDao = userDao;
        this.tokenService = tokenService;
    }

    // 회원가입(POST)
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        BaseResponseStatus status = isRegexPostUser(postUserReq);
        if (status != null){
            throw new BaseException(status);
        }
        if (userDao.checkEmail(postUserReq.getEmail())){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        String userCode = createUserCode();
        int userId = userDao.createUser(postUserReq, userCode);
        try {
            String jwt = tokenService.createJwt(userId);
            return new PostUserRes(userId, userCode, jwt);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 로그인
    public String login(String email) throws BaseException {
        if (email == null || email.isEmpty()) {
            throw new BaseException(POST_USERS_EMPTY_EMAIL);
        }
        else if (!isRegexEmail(email)) {
            throw new BaseException(POST_USERS_INVALID_EMAIL);
        }
        try {
            if(userDao.checkEmail(email)){
                int userId = userDao.getUserIdByEmail(email);
                return tokenService.createJwt(userId);
            }
            throw new BaseException(NOT_EXIST_EMAIL);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }

    }

    // User 전체 조회
    public List<User> getUsers() throws BaseException {
        try {
            return userDao.getUsers();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // userId로 User 조회
    public GetUserRes getUser(int userId) throws BaseException {
        try {
            return userDao.getUser(userId);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // User 삭제
    public void deleteUser(int userId) throws BaseException {
        try {
            userDao.deleteUser(userId);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // User 수정
    public void modifyUser(int userId, int type, String str) throws BaseException {
        if (type == 1 && !isRegexNickname(str)) {
            throw new BaseException(PATCH_USERS_INVALID_NICKNAME);
        }
        else if (type == 2 && !isRegexDay(str)){
            throw new BaseException(PATCH_USERS_INVALID_BIRTHDAY);
        }
        else if (type == 3 && !isRegexDay(str)){
            throw new BaseException(PATCH_USERS_INVALID_FIRSTMETDAY);
        }
        try {
            userDao.modifyUser(userId, type, str);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
