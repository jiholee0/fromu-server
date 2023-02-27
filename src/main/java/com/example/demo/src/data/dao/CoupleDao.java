package com.example.demo.src.data.dao;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dto.couple.CoupleRes;
import com.example.demo.src.data.dto.couple.GetCoupleMatchRes;
import com.example.demo.src.data.dto.couple.PostCoupleReq;
import com.example.demo.src.data.entity.Couple;
import com.example.demo.src.data.entity.CoupleRepository;
import com.example.demo.src.data.entity.User;
import com.example.demo.src.data.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.example.demo.config.BaseResponseStatus.NOT_EXIST_DATA;
import static com.example.demo.config.BaseResponseStatus.POST_COUPLES_EXISTS_USER;

@Repository
public class CoupleDao {
    @Autowired
    CoupleRepository coupleRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional
    // 커플 등록(POST)
    public CoupleRes createCouple(int userId, String partnerCode) throws BaseException {
        Optional<User> partner = userRepository.findByUserCode(partnerCode);
        int partnerId = partner.get().getUserId();
        if (checkUserId(partnerId)){
            throw new BaseException(POST_COUPLES_EXISTS_USER);
        }
        PostCoupleReq postCoupleReq = new PostCoupleReq();
        Couple couple = postCoupleReq.toEntity(userId, partnerId);
        coupleRepository.save(couple);

        return new CoupleRes(
                couple.getCoupleId(),
                userRepository.findById(userId).get().getNickname(),
                partner.get().getNickname());
    }

    @Transactional
    // userId로 커플에 존재하는지 확인
    public boolean checkUserId(int userId) {
        Optional<Couple> couple = coupleRepository.findByUserId1OrUserId2(userId, userId);
        return couple.isPresent();
    }

    // 매칭 여부 확인
    @Transactional
    public GetCoupleMatchRes getCoupleMatchRes(int userId) {
        CoupleRes coupleRes = null;
//        Optional<Couple> couple = coupleRepository.findByUser1IdOrUser2Id(userId, userId);
//        if (!couple.isPresent()) {
//            return new GetCoupleMatchRes(false, null);
//        }
//        if(couple.get().getUser1Id() == userId){
//            coupleRes = new CoupleRes(
//                    couple.get().getCoupleId(),
//                    userRepository.findById(userId).get().getNickname(),
//                    userRepository.findById(couple.get().getUser2Id()).get().getNickname()
//            );
//        } else if (couple.get().getUser2Id() == userId) {
//            coupleRes = new CoupleRes(
//                    couple.get().getCoupleId(),
//                    userRepository.findById(userId).get().getNickname(),
//                    userRepository.findById(couple.get().getUser1Id()).get().getNickname()
//            );
//        }
        return new GetCoupleMatchRes(true, coupleRes);
    }

    @Transactional
    // 커플 전체 조회
    public List<Couple> getCouples(){ return coupleRepository.findAll(); }

    @Transactional
    // userId로 커플 조회
    public Couple getCoupleByUserId(int userId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        return couple.get();
    }

    @Transactional
    // coupleId로 커플 조회
    public Couple getCoupleByCoupleId(int coupleId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findById(coupleId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        return couple.get();
    }

    @Transactional
    public void modifyFirstMetDay(int userId, String str) throws BaseException{
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        couple.get().modifyFirstMetDay(str);
    }

    @Transactional
    public void deleteCouple(int userId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        coupleRepository.deleteById(couple.get().coupleId);
    }
}
