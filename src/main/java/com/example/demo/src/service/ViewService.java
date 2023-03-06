package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.data.dao.CoupleDao;
import com.example.demo.src.data.dao.DiarybookDao;
import com.example.demo.src.data.dao.UserDao;
import com.example.demo.src.data.dto.view.DiarybookDto;
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
        Diarybook diarybook = diarybookDao.getDiarybookByCoupleId(couple.getCoupleId());

        try {
            int diarybookStatus = 0;
            Date date = new Date();
            if(diarybook.getTurnUserId()==userId){
                if(diarybook.getTurnTime()==null || date.after(diarybook.getTurnTime())){
                    diarybookStatus = 1;
                }
                else {diarybookStatus = 2;}
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return new MainViewRes(nickname,
                        partnerNickname,
                        CommonUtils.calDDay(couple.getFirstMetDay()),
                        diarybookStatus,
                        new DiarybookDto(diarybook.getDiarybookId(),
                                diarybook.getCoverNum(),
                                diarybook.getName(),
                                diarybook.getTurnUserId(),
                                sdf.format(diarybook.getTurnTime()),
                                diarybook.getImageUrl(),
                                diarybook.isDeleteFlag())
                );
            } else{
                return new MainViewRes(nickname,
                        partnerNickname,
                        CommonUtils.calDDay(couple.getFirstMetDay()),
                        diarybookStatus,
                        null);
            }
        } catch (NullPointerException nullPointerException){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
