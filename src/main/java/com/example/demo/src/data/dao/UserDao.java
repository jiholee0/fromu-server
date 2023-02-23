package com.example.demo.src.data.dao;


import com.example.demo.src.data.dto.user.GetUserRes;
import com.example.demo.src.data.dto.user.GetUsersRes;
import com.example.demo.src.data.dto.user.PostKakaoRes;
import com.example.demo.src.data.dto.user.PostUserReq;
import com.example.demo.src.data.entity.User;
import com.example.demo.src.data.entity.UserRepository;
import com.example.demo.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;

@Repository
public class UserDao {

    @Autowired
    UserRepository userRepository;

    // 회원가입(POST)
    public int createUser(PostUserReq postUserReq, String userCode) {
        User user = postUserReq.toEntity(userCode);
        return user.getUserId();
    }

    // 이메일 존재 여부
    public boolean checkEmail(String email){
        // TODO : USER_TB에서 email이 존재하는지 체크
        return false;
    }

    // email로 userId 조회
    public int getUserIdByEmail(String email){
        // TODO : USER_TB에서 email로 userId 찾기
        return 0;
    }

    @Transactional
    // User 전체 조회
    public List<User> getUsers() {
        List<User> usersList = userRepository.findAll();
        return usersList;
    }

    // userId로 User 조회
    public GetUserRes getUser(int userId) throws ParseException {
        User user = userRepository.findById(userId);
        GetUserRes getUserRes = new GetUserRes(user);
        return getUserRes;
    }

    public void deleteUser(int userId) {
        // TODO : USER_ID로 USER 정보 삭제(soft delete)
    }
}
