package com.example.demo.src.data.dao;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dto.letter.GetLetterRes;
import com.example.demo.src.data.dto.letter.PatchReadLetterRes;
import com.example.demo.src.data.dto.letter.PostLetterReq;
import com.example.demo.src.data.dto.letter.PostLetterRes;
import com.example.demo.src.data.entity.Couple;
import com.example.demo.src.data.entity.CoupleRepository;
import com.example.demo.src.data.entity.Letter;
import com.example.demo.src.data.entity.LetterRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

import static com.example.demo.config.BaseResponseStatus.*;

@Repository
public class LetterDao {
    @Autowired
    LetterRepository letterRepository;
    @Autowired
    CoupleRepository coupleRepository;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    Date date = new Date();

    @Transactional
    public PostLetterRes sendLetter(int userId, PostLetterReq postLetterReq, Couple receiveCouple) throws BaseException {
        Optional<Couple> sendCouple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        date = new Date();
        Letter letter = postLetterReq.toEntity(0,userId,sendCouple.get().getCoupleId(),receiveCouple.getCoupleId(), date);
        letterRepository.save(letter);
        return new PostLetterRes(letter.getLetterId(),sendCouple.get().getMailboxName(),receiveCouple.getMailboxName());
    }

    @Transactional
    public PostLetterRes sendLetterReply(int userId, int letterId, PostLetterReq postLetterReq) throws BaseException {
        Optional<Couple> sendCouple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        Optional<Letter> refLetter = Optional.of(letterRepository.findById(letterId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_LETTER)
        ));
        if (refLetter.get().getRefLetterId()!=0){
            throw new BaseException(POST_LETTERS_ALREADY_REPLY);
        }
        Optional<Couple> receiveCouple = Optional.of(coupleRepository.findById(refLetter.get().sendCoupleId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        date = new Date();
        Letter letter = postLetterReq.toEntity(letterId,userId,sendCouple.get().getCoupleId(),receiveCouple.get().getCoupleId(), date);
        letterRepository.save(letter);
        return new PostLetterRes(letter.getLetterId(),sendCouple.get().getMailboxName(),receiveCouple.get().getMailboxName());
    }

    @Transactional
    public PatchReadLetterRes readLetter(int userId, int letterId) throws BaseException {
        Optional<Letter> letter = Optional.of(letterRepository.findById(letterId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_LETTER)
        ));
        Optional<Couple> sendCouple = Optional.of(coupleRepository.findById(letter.get().getSendCoupleId()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        Optional<Couple> receiveCouple = Optional.of(coupleRepository.findById(letter.get().getReceiveCoupleId()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        int status = -1;
        boolean replyFlag = false;
        boolean scoreFlag = false;

        if(sendCouple.get().getUserId1()==userId || sendCouple.get().getUserId2()==userId){
            status = 1;
        } else if(receiveCouple.get().getUserId1()==userId || receiveCouple.get().getUserId2()==userId){
            if(letter.get().getRefLetterId()==0) {
                status = 0;
                Optional<Letter> checkLetterReply = letterRepository.findByRefLetterId(letterId);
                if (checkLetterReply.isPresent()){
                    replyFlag = true;
                }
            }
            else {
                status = 2;
                if(letter.get().getScore()!=-1){scoreFlag = true;}
            }
            letter.get().read();
        }
        return new PatchReadLetterRes(letterId,letter.get().getStampNum(),letter.get().getContent(),
                sendCouple.get().getMailboxName(),receiveCouple.get().getMailboxName(),letter.get().getCreateDate(),status,replyFlag,scoreFlag);
    }

    @Transactional
    public List<GetLetterRes> getSendLetterList(int userId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId,userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        List<Letter> letterList = letterRepository.findBySendCoupleId(couple.get().getCoupleId());
        List<GetLetterRes> resList = new ArrayList<>();
        for(Letter letter : letterList){
            Optional<Couple> receiveCouple = Optional.of(coupleRepository.findById(letter.getReceiveCoupleId()).orElseThrow(
                    () -> new BaseException(NOT_EXIST_DATA_COUPLE)
            ));
            resList.add(new GetLetterRes(
                    letter.getLetterId(),
                    receiveCouple.get().getMailboxName(),
                    letter.getCreateDate(),
                    letter.isReadFlag()));
        }
        return resList;
    }
    public List<GetLetterRes> getReceiveLetterList(int userId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId,userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        List<Letter> letterList = letterRepository.findByReceiveCoupleId(couple.get().getCoupleId());
        List<GetLetterRes> resList = new ArrayList<>();
        for(Letter letter : letterList){
            Optional<Couple> sendCouple = Optional.of(coupleRepository.findById(letter.getSendCoupleId()).orElseThrow(
                    () -> new BaseException(NOT_EXIST_DATA_COUPLE)
            ));
            resList.add(new GetLetterRes(
                    letter.getLetterId(),
                    sendCouple.get().getMailboxName(),
                    letter.getCreateDate(),
                    letter.isReadFlag()));
        }
        return resList;
    }

    @Transactional
    public Map<Integer, Integer> scoreLetter(int userId, int letterId, int score) throws BaseException {
        Optional<Letter> letter = Optional.of(letterRepository.findById(letterId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_LETTER)
        ));
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId,userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        if(letter.get().getReceiveCoupleId()!=couple.get().getCoupleId()){
            throw new BaseException(FAIL_TO_SCORE_INVALID_COUPLE);
        }
        if(letter.get().getScore()!=-1){
            throw new BaseException(FAIL_TO_SCORE_ALREADY);
        }
        letter.get().score(score);
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0,letter.get().getReceiveCoupleId());
        map.put(1,letter.get().getSendCoupleId());
        return map;
    }
}
