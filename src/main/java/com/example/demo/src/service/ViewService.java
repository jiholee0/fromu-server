package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dao.*;
import com.example.demo.src.data.dto.view.DiarybookDto;
import com.example.demo.src.data.dto.view.MailboxViewRes;
import com.example.demo.src.data.dto.view.MainViewRes;
import com.example.demo.src.data.dto.view.NoticeViewRes;
import com.example.demo.src.data.entity.Couple;
import com.example.demo.src.data.entity.Diarybook;
import com.example.demo.src.data.entity.NoticeRepository;
import com.example.demo.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ViewService {
    private final DiarybookDao diarybookDao;
    private final CoupleDao coupleDao;
    private final ShopDao shopDao;
    private final LetterDao letterDao;
    private final UserDao userDao;

    @Autowired
    public ViewService(DiarybookDao diarybookDao, CoupleDao coupleDao, ShopDao shopDao, LetterDao letterDao, UserDao userDao){
        this.diarybookDao = diarybookDao;
        this.coupleDao = coupleDao;
        this.shopDao = shopDao;
        this.letterDao = letterDao;
        this.userDao = userDao;
    }

    public MainViewRes mainView(int userId) throws BaseException {
        String nickname = userDao.getUser(userId).getNickname();
        String partnerNickname;
        Couple couple = coupleDao.getCoupleByUserId(userId);
        if(couple.getUserId1()==userId){
            partnerNickname = userDao.getUser(couple.getUserId2()).getNickname();
        } else {
            partnerNickname = userDao.getUser(couple.getUserId1()).getNickname();
        }
        int diarybookStatus = 0;
        if(!diarybookDao.isExistDiarybook(couple.getCoupleId())){
            return new MainViewRes(nickname, partnerNickname, CommonUtils.calDDay(couple.getFirstMetDay()),diarybookStatus,null);
        }
        Diarybook diarybook = diarybookDao.getDiarybookByCoupleId(couple.getCoupleId());
        Date date = new Date();
        if(diarybook.getTurnUserId()==userId){
            if(diarybook.getTurnTime()==null || date.after(diarybook.getTurnTime())){
                diarybookStatus = 1;
            }
            else {diarybookStatus = 2;}
        } else{
            if(diarybook.getTurnTime()==null || date.after(diarybook.getTurnTime())){
                diarybookStatus = 4;
            } else {diarybookStatus = 3;}
        }
        return new MainViewRes(nickname,
                partnerNickname,
                CommonUtils.calDDay(couple.getFirstMetDay()),
                diarybookStatus,
                new DiarybookDto(diarybook.getDiarybookId(),
                        diarybook.getCoverNum(),
                        diarybook.getName(),
                        diarybook.getImageUrl(),
                        diarybook.isWriteFlag())
        );
    }

    public MailboxViewRes mailboxView(int userId) throws BaseException {
        Couple couple = coupleDao.getCoupleByUserId(userId);
        int newLetterId = letterDao.getNewLetterId(userId);
        return new MailboxViewRes(couple.getCoupleId(), couple.getMailboxName(), newLetterId);
    }

    public int getFrom(int userId) throws BaseException {
        return shopDao.getFromByUserId(userId);
    }

    public List<Integer> getStamp(int userId) throws BaseException {
        return shopDao.getStampByUserId(userId);
    }

    public List<NoticeViewRes> getNotice(int userId) throws BaseException {
        return coupleDao.getNotice(userId);
    }
}
