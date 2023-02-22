package com.example.demo.src.data.dao;


import com.example.demo.src.data.dto.user.GetUserRes;
import com.example.demo.src.data.dto.user.PostKakaoRes;
import com.example.demo.src.data.dto.user.PostUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    // 회원가입(POST)
    public int createUser(PostUserReq postUserReq, String userCode) {
        // TODO : 가입한 카카오 email, nickname, birthday, gender, firstMetDay, userCode DB에 추가
        return 0;
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

    // User 전체 조회
    public List<GetUserRes> getUsers() {
        // TODO : USER_TB에서 모든 USER 정보 조회
        return null;
    }

    // userId로 User 조회
    public GetUserRes getUser(int userId) {
        // TODO : USER_ID로 USER 정보 조회
        return null;
    }

    public void deleteUser(int userId) {
        // TODO : USER_ID로 USER 정보 삭제(soft delete)
    }
}
