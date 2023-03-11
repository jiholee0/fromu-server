package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dao.CoupleDao;
import com.example.demo.src.data.dao.LetterDao;
import com.example.demo.src.data.dao.ShopDao;
import com.example.demo.src.data.dto.letter.*;
import com.example.demo.src.data.entity.Couple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.example.demo.config.BaseResponseStatus.PATCH_COUPLES_INVALID_STAMPNUM;

@Service
public class LetterService {
    private final LetterDao letterDao;
    private final CoupleDao coupleDao;
    private final ShopDao shopDao;
    @Autowired
    public LetterService(LetterDao letterDao, CoupleDao coupleDao, ShopDao shopDao){
        this.letterDao = letterDao;
        this.coupleDao = coupleDao;
        this.shopDao = shopDao;
    }

    public PostLetterRes sendLetter(int userId, PostLetterReq postLetterReq) throws BaseException {
        Couple receiveCouple = coupleDao.getRandomCouple(userId);
        if(postLetterReq.getStampNum()>=1 && postLetterReq.getStampNum() <=6){
            shopDao.useStamp(userId,postLetterReq.getStampNum());
            return letterDao.sendLetter(userId, postLetterReq, receiveCouple);
        }
        throw new BaseException(PATCH_COUPLES_INVALID_STAMPNUM);
    }

    public PostLetterRes sendLetterReply(int userId, int letterId, PostLetterReq postLetterReq) throws BaseException {
        return letterDao.sendLetterReply(userId, letterId, postLetterReq);
    }

    public PatchReadLetterRes readLetter(int userId, int letterId) throws BaseException {
        return letterDao.readLetter(userId, letterId);
    }

    public List<GetLetterRes> getSendLetterList(int userId) throws BaseException {
        return letterDao.getSendLetterList(userId);
    }
    public List<GetLetterRes> getReceiveLetterList(int userId) throws BaseException {
        return letterDao.getReceiveLetterList(userId);
    }

    public PatchScoreRes scoreLetter(int userId, int letterId, int score) throws BaseException {
        Map<Integer, Integer> coupleId = letterDao.scoreLetter(userId, letterId, score);
        Map<Integer, Integer> coupleFromCount = shopDao.scoreLetter(coupleId.get(0), coupleId.get(1), score);
        return new PatchScoreRes(coupleId.get(0), coupleFromCount.get(0), coupleId.get(1), coupleFromCount.get(1));
    }

    public int report(int userId, int letterId, PostReportReq postReportReq) throws BaseException {
        return letterDao.report(userId, letterId, postReportReq);
    }
}
