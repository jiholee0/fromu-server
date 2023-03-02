package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.data.dto.user.*;
import com.example.demo.src.data.dao.UserDao;
import com.example.demo.src.data.entity.User;
import com.example.demo.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
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
        try {
            String userCode = createUserCode();
            while (userDao.checkUserCode(userCode)){
                userCode = createUserCode();
            }
            int userId = userDao.createUser(postUserReq, userCode);

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
        if(userDao.checkEmail(email)){
            User user = userDao.getUserByEmail(email);
            return tokenService.createJwt(user.getUserId());
        }
        throw new BaseException(NOT_EXIST_EMAIL);
    }

    // User 전체 조회
    public List<User> getUsers() throws BaseException {
        return userDao.getUsers();
    }

    // userId로 User 조회
    public User getUser(int userId) throws BaseException{
        return userDao.getUser(userId);
    }

    // User 삭제
    public void deleteUser(int userId) throws BaseException {
        userDao.deleteUser(userId);
    }

    // User 수정
    public void modifyUser(int userId, int type, String str) throws BaseException {
        if (type == 1 && !isRegexNickname(str)) {
            throw new BaseException(PATCH_USERS_INVALID_NICKNAME);
        }
        else if (type == 2 && !isRegexDay(str)){
            throw new BaseException(PATCH_USERS_INVALID_BIRTHDAY);
        }
        userDao.modifyUser(userId, type, str);

    }
}
