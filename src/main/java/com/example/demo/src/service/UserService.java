package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.data.dao.CoupleDao;
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
    private final CoupleDao coupleDao;
    private final TokenService tokenService;

    @Autowired
    public UserService(UserDao userDao, CoupleDao coupleDao, TokenService tokenService) {
        this.userDao = userDao;
        this.coupleDao = coupleDao;
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
        while (userDao.checkUserCode(userCode)){
            userCode = createUserCode();
        }
        int userId = userDao.createUser(postUserReq, userCode);

        String jwt = tokenService.createJwt(userId);
        String refreshToken = tokenService.createRefreshToken(userId);
        userDao.saveRefreshToken(userId, refreshToken);
        try {
            return new PostUserRes(userId, userCode, jwt, refreshToken);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 자동로그인
    public UserInfo loginRefreshToken(String accessToken) throws BaseException {
        int userId = tokenService.getUserId();
        User user = userDao.getUser(userId);
        if (user.getRefreshToken().equals(accessToken)){
            // refresh token 유효 -> jwt 재발급(access, refresh)
            String jwt = tokenService.createJwt(userId);
            String refreshToken = tokenService.createRefreshToken(userId);
            userDao.saveRefreshToken(userId, refreshToken);
            UserInfo userInfo = new UserInfo(
                    coupleDao.checkUserId(userId),
                    coupleDao.isSetMailboxName(userId),
                    userId,
                    jwt,
                    refreshToken,
                    null,
                    user.getUserCode()
            );
            return userInfo;
        } else {
            throw new BaseException(INVALID_JWT);
        }
    }

    // 로그인
    public UserInfo login(String email) throws BaseException {
        if (email == null || email.isEmpty()) {
            throw new BaseException(POST_USERS_EMPTY_EMAIL);
        }
        else if (!isRegexEmail(email)) {
            throw new BaseException(POST_USERS_INVALID_EMAIL);
        }
        if(userDao.checkEmail(email)){
            User user = userDao.getUserByEmail(email);
            String jwt = tokenService.createJwt(user.getUserId());
            UserInfo userInfo = new UserInfo(
                    coupleDao.checkUserId(user.getUserId()),
                    coupleDao.isSetMailboxName(user.getUserId()),
                    user.getUserId(),
                    jwt,
                    user.getRefreshToken(),
                    email,
                    user.getUserCode()
            );
            return userInfo;
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

    public PatchUserRes saveDeviceToken(int userId, String str) throws  BaseException {
        return userDao.saveDeviceToken(userId, str);
    }
}
