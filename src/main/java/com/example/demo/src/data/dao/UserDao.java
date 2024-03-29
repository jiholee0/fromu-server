package com.example.demo.src.data.dao;


import com.example.demo.config.BaseException;
import com.example.demo.src.data.dto.user.PatchUserRes;
import com.example.demo.src.data.dto.user.PostUserReq;
import com.example.demo.src.data.entity.Couple;
import com.example.demo.src.data.entity.CoupleRepository;
import com.example.demo.src.data.entity.User;
import com.example.demo.src.data.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.example.demo.config.BaseResponseStatus.*;

@Repository
public class UserDao {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private CoupleRepository coupleRepository;

    @Transactional
    // 회원가입(POST)
    public int createUser(PostUserReq postUserReq, String userCode) {
        User user = postUserReq.toEntity(userCode);
        userRepository.save(user);
        return user.getUserId();
    }

    @Transactional
    // 이메일 존재 여부
    public boolean checkEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

    @Transactional
    // email로 userId 조회
    public User getUserByEmail(String email) throws BaseException {
        Optional<User> user = Optional.of(userRepository.findByEmail(email).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        try {
            return user.get();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    // User 전체 조회
    public List<User> getUsers() throws BaseException {
        try {
            return userRepository.findAll();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    // userId로 User 조회
    public User getUser(int userId) throws BaseException {
        Optional<User> user = Optional.of(userRepository.findById(userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        try {
            return user.get();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void deleteUser(int userId) throws BaseException {
        Optional<Couple> couple = coupleRepository.findByUserId1OrUserId2(userId, userId);
        couple.ifPresent(value -> coupleRepository.deleteById(value.getCoupleId()));
        try {
            userRepository.deleteById(userId);
        } catch (IllegalArgumentException exception){
            throw new BaseException(NOT_EXIST_DATA);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void modifyUser(int userId, int type, String str) throws BaseException {
        Optional<User> user = Optional.of(userRepository.findById(userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        try {
            switch (type) {
                case 1:
                    user.get().modifyNickname(str);
                    break;
                case 2:
                    user.get().modifyBirthday(str);
                    break;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // userCode 존재 여부
    @Transactional
    public boolean checkUserCode(String userCode) throws BaseException {
        Optional<User> user = userRepository.findByUserCode(userCode);
        return user.isPresent();
    }
    
    // userCode로 userId 조회
    @Transactional
    public int getUserIdByUserCode(String userCode) throws BaseException {
        Optional<User> user = Optional.of(userRepository.findByUserCode(userCode).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        return user.get().getUserId();
    }

    @Transactional
    public void saveRefreshToken(int userId, String refreshToken) throws BaseException {
        Optional<User> user = Optional.of(userRepository.findById(userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        try {
            user.get().saveRefreshToken(refreshToken);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public PatchUserRes saveDeviceToken(int userId, String str) throws BaseException {
        Optional<User> user = Optional.of(userRepository.findById(userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        try {
            user.get().saveDeviceToken(str);
            return new PatchUserRes(user.get().getUserId());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void logout(int userId) throws BaseException {
        Optional<User> user = Optional.of(userRepository.findById(userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        try {
            user.get().saveDeviceToken("");
            user.get().saveRefreshToken("");
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
