package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dao.CoupleDao;
import com.example.demo.src.data.dao.LetterDao;
import com.example.demo.src.data.dto.letter.GetLetterRes;
import com.example.demo.src.data.dto.letter.PatchReadLetterRes;
import com.example.demo.src.data.dto.letter.PostLetterReq;
import com.example.demo.src.data.dto.letter.PostLetterRes;
import com.example.demo.src.data.entity.Couple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LetterService {
    private final LetterDao letterDao;
    private final CoupleDao coupleDao;
    @Autowired
    public LetterService(LetterDao letterDao, CoupleDao coupleDao){
        this.letterDao = letterDao;
        this.coupleDao = coupleDao;
    }

    public PostLetterRes sendLetter(int userId, PostLetterReq postLetterReq) throws BaseException {
        Couple receiveCouple = coupleDao.getRandomCouple(userId);
        // TODO : 우표 1개 차감
        return letterDao.sendLetter(userId, postLetterReq, receiveCouple);
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
}
