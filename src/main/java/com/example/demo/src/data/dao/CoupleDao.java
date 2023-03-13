package com.example.demo.src.data.dao;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dto.couple.CoupleRes;
import com.example.demo.src.data.dto.couple.GetCoupleMatchRes;
import com.example.demo.src.data.dto.couple.PostCoupleReq;
import com.example.demo.src.data.entity.Couple;
import com.example.demo.src.data.entity.CoupleRepository;
import com.example.demo.src.data.entity.User;
import com.example.demo.src.data.entity.UserRepository;
import com.example.demo.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.config.BaseResponseStatus.*;

@Repository
public class CoupleDao {
    @Autowired
    CoupleRepository coupleRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional
    // 커플 등록(POST)
    public CoupleRes createCouple(int userId, PostCoupleReq postCoupleReq) throws BaseException {

        Optional<User> partner = userRepository.findByUserCode(postCoupleReq.getPartnerCode());
        if (!partner.isPresent()) {
            throw new BaseException(NOT_EXIST_DATA);
        }
        int partnerId = partner.get().getUserId();
        Optional<Couple> checkPartner = coupleRepository.findByUserId1OrUserId2(partnerId, partnerId);
        if (checkPartner.isPresent()) {
            throw new BaseException(POST_COUPLES_EXISTS_USER);
        }
        try {
            Couple couple = postCoupleReq.toEntity(userId, partnerId);
            coupleRepository.save(couple);
            return new CoupleRes(
                    couple.getCoupleId(),
                    false,
                    userRepository.findById(userId).get().getNickname(),
                    partner.get().getNickname());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    // userId로 커플에 존재하는지 확인
    public boolean checkUserId(int userId) {
        Optional<Couple> couple = coupleRepository.findByUserId1OrUserId2(userId, userId);
        return couple.isPresent();
    }

    // 매칭 여부 확인
    @Transactional
    public GetCoupleMatchRes getCoupleMatchRes(int userId) throws BaseException {
        try {
            CoupleRes coupleRes = null;
            Optional<Couple> couple = coupleRepository.findByUserId1OrUserId2(userId, userId);
            if (!couple.isPresent()) {
                return new GetCoupleMatchRes(false, null,0);
            }
            boolean isSetMailboxName = couple.get().mailboxName != null && !couple.get().mailboxName.equals("");
            if (couple.get().getUserId1() == userId) {
                coupleRes = new CoupleRes(
                        couple.get().getCoupleId(),
                        isSetMailboxName,
                        userRepository.findById(userId).get().getNickname(),
                        userRepository.findById(couple.get().getUserId2()).get().getNickname()
                );
            } else if (couple.get().getUserId2() == userId) {
                coupleRes = new CoupleRes(
                        couple.get().getCoupleId(),
                        isSetMailboxName,
                        userRepository.findById(userId).get().getNickname(),
                        userRepository.findById(couple.get().getUserId1()).get().getNickname()
                );
            }
            int dDay = CommonUtils.calDDay( couple.get().getFirstMetDay());
            return new GetCoupleMatchRes(true, coupleRes, dDay);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    // 커플 전체 조회
    public List<Couple> getCouples() {
        return coupleRepository.findAll();
    }

    @Transactional
    // userId로 커플 조회
    public Couple getCoupleByUserId(int userId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        try {
            return couple.get();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    // coupleId로 커플 조회
    public Couple getCoupleByCoupleId(int coupleId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findById(coupleId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        try {
            return couple.get();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public int modifyFirstMetDay(int userId, String str) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        try {
            couple.get().modifyFirstMetDay(str);
            return couple.get().getCoupleId();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public int modifyMailbox(int userId, String str) throws BaseException {
        Optional<Couple> checkMailboxCouple = coupleRepository.findByMailboxName(str);
        if (checkMailboxCouple.isPresent()) {
            throw new BaseException(PATCH_COUPLES_EXISTS_MAILBOX);
        }
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        try {
            couple.get().modifyMailbox(str);
            return couple.get().getCoupleId();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public int modifyPushMessage(int userId, String str) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        try {
            couple.get().modifyPushMessage(str);
            return couple.get().getCoupleId();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public int deleteCouple(int userId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        try {
            coupleRepository.deleteById(couple.get().coupleId);
            return couple.get().getCoupleId();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 우편함 이름 설정 flag
    @Transactional
    public boolean isSetMailboxName(int userId) throws BaseException {
        Optional<Couple> couple = coupleRepository.findByUserId1OrUserId2(userId, userId);
        if (!couple.isPresent()) return false;
        try {
            return couple.get().mailboxName != null && !couple.get().mailboxName.equals("");

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public boolean checkMailbox(String mailbox) throws BaseException {
        try {
            Optional<Couple> couple = coupleRepository.findByMailboxName(mailbox);
            return couple.isPresent();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public String getPushMessage(int userId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        return couple.get().getPushMessage();
    }

    @Transactional
    public String getPartnerDeviceToken(int userId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        int partnerId;
        if (userId == couple.get().getUserId1()) {
            partnerId = couple.get().getUserId2();
        } else {
            partnerId = couple.get().getUserId1();
        }
        Optional<User> user = Optional.of(userRepository.findById(partnerId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        return user.get().getDeviceToken();
    }


    @Transactional
    public Couple getRandomCouple(int userId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        return coupleRepository.findRandomCouple(couple.get().getCoupleId());
    }

    @Transactional
    public String getPartnerNickname(int userId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        int partnerId;
        if (userId == couple.get().getUserId1()) {
            partnerId = couple.get().getUserId2();
        } else {
            partnerId = couple.get().getUserId1();
        }
        Optional<User> user = Optional.of(userRepository.findById(partnerId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        return user.get().getNickname();
    }
}
