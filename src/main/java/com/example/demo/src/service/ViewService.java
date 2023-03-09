package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.data.dao.CoupleDao;
import com.example.demo.src.data.dao.DiarybookDao;
import com.example.demo.src.data.dao.UserDao;
import com.example.demo.src.data.dto.view.DiarybookDto;
import com.example.demo.src.data.dto.view.MailboxViewRes;
import com.example.demo.src.data.dto.view.MainViewRes;
import com.example.demo.src.data.entity.Couple;
import com.example.demo.src.data.entity.Diarybook;
import com.example.demo.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ViewService {
    private final DiarybookDao diarybookDao;
    private final CoupleDao coupleDao;
    private final UserDao userDao;

    @Autowired
    public ViewService(DiarybookDao diarybookDao, CoupleDao coupleDao, UserDao userDao){

        this.diarybookDao = diarybookDao;
        this.coupleDao = coupleDao;
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
                        diarybook.isWriteFlag()
                )
        );
    }

    public MailboxViewRes mailboxView(int userId) throws BaseException {
        Couple couple = coupleDao.getCoupleByUserId(userId);
        int newLetterId = 0; //letterDao.getNewLetterId(userId);
        return new MailboxViewRes(couple.getCoupleId(), couple.getMailboxName(), newLetterId);
    }
}
