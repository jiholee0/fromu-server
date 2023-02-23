package com.example.demo.src.data.dao;


import com.example.demo.src.data.dto.user.GetUserRes;
import com.example.demo.src.data.dto.user.PostUserReq;
import com.example.demo.src.data.entity.User;
import com.example.demo.src.data.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

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
    public int getUserIdByEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        return user.get().getUserId();
    }

    @Transactional
    // User 전체 조회
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    // userId로 User 조회
    public GetUserRes getUser(int userId) throws ParseException {
        User user = userRepository.findById(userId);
        return new GetUserRes(user);
    }

    @Transactional
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }
}
