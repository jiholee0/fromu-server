package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dao.ScheduleDao;
import com.example.demo.src.data.dto.schedule.ScheduleReq;
import com.example.demo.src.data.dto.schedule.ScheduleRes;
import com.example.demo.src.data.entity.Schedule;
import com.example.demo.src.data.entity.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexDay;

@Service
public class ScheduleService {
    private final ScheduleDao scheduleDao;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleDao scheduleDao, ScheduleRepository scheduleRepository) {
        this.scheduleDao = scheduleDao;
        this.scheduleRepository = scheduleRepository;
    }


    public int createSchedule(int userId, ScheduleReq postScheduleReq) throws BaseException {
        if(!isRegexDay(postScheduleReq.getDate())) throw new BaseException(POST_SCHEDULES_INVALID_DATE);
        return scheduleDao.createSchedule(userId, postScheduleReq);
    }
    public List<ScheduleRes> getSchedulesByMonth(int userId, String month) throws BaseException {
        return scheduleDao.getSchedulesByMonth(userId, month);
    }

    public List<ScheduleRes> getSchedulesByDate(int userId, String month, String date) throws BaseException {
        return scheduleDao.getSchedulesByDate(userId, month, date);
    }

    public List<Integer> getDateListByMonth(int userId, String month) throws BaseException {
        return scheduleDao.getDateListByMonth(userId, month);
    }

    public void modifySchedule(int userId, int scheduleId, ScheduleReq patchScheduleReq) throws BaseException {
        if(!isRegexDay(patchScheduleReq.getDate())) throw new BaseException(PATCH_SCHEDULES_INVALID_DATE);
        Optional<Schedule> schedule = Optional.of(scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        if(schedule.get().getUserId()!=userId) throw new BaseException(INVALID_USER_JWT);
        try{
            schedule.get().modifyContent(patchScheduleReq.getContent());
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteSchedule(int userId, int scheduleId) throws BaseException {
        Optional<Schedule> schedule = Optional.of(scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA)
        ));
        if(schedule.get().getUserId()!=userId) throw new BaseException(INVALID_USER_JWT);
        try{
            scheduleRepository.deleteById(scheduleId);
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
