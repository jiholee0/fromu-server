package com.example.demo.src.data.dao;


import com.example.demo.config.BaseException;
import com.example.demo.src.data.dto.user.PostUserReq;
import com.example.demo.src.data.entity.User;
import com.example.demo.src.data.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import static com.example.demo.config.BaseResponseStatus.*;

@Repository
public class UserDao {

    @Autowired
    UserRepository userRepository;

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
    public int getUserIdByEmail(String email) throws BaseException {
        Optional<User> user = Optional.of(userRepository.findByEmail(email).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        return user.get().getUserId();
    }

    @Transactional
    // User 전체 조회
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    // userId로 User 조회
    public User getUser(int userId) throws ParseException, BaseException {
        Optional<User> user = Optional.of(userRepository.findById(userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        return user.get();
    }

    @Transactional
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public void modifyUser(int userId, int type, String str) throws BaseException {
        Optional<User> user = Optional.of(userRepository.findById(userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        switch (type) {
            case 1 : user.get().modifyNickname(str); break;
            case 2 : user.get().modifyBirthday(str); break;
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
    public User findByUserCode(String userCode) throws BaseException {
        Optional<User> user = Optional.of(userRepository.findByUserCode(userCode).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        return user.get();
    }
}
