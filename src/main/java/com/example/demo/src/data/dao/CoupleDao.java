package com.example.demo.src.data.dao;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dto.couple.PostCoupleReq;
import com.example.demo.src.data.dto.couple.PostCoupleRes;
import com.example.demo.src.data.entity.Couple;
import com.example.demo.src.data.entity.CoupleRepository;
import com.example.demo.src.data.entity.User;
import com.example.demo.src.data.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.example.demo.config.BaseResponseStatus.NOT_EXIST_DATA;

@Repository
public class CoupleDao {
    @Autowired
    CoupleRepository coupleRepository;
    @Autowired
    UserRepository userRepository;

    @Transactional
    // 커플 등록(POST)
    public PostCoupleRes createCouple(PostCoupleReq postCoupleReq, int user2Id) throws BaseException {
        Couple couple = postCoupleReq.toEntity(user2Id);
        coupleRepository.save(couple);
        Optional<User> user = Optional.of(userRepository.findById(user2Id).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        return new PostCoupleRes(couple.getCoupleId(), user.get().getNickname());
    }

    @Transactional
    // userId로 커플에 존재하는지 확인
    public boolean checkUserId(int user1Id, int user2Id) {
        Optional<Couple> couple1 = coupleRepository.findByUser1Id(user1Id);
        Optional<Couple> couple2 = coupleRepository.findByUser2Id(user2Id);
        return couple1.isPresent() || couple2.isPresent();
    }
}
