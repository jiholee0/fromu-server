package com.example.demo.src.data.dto.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ScheduleRes {
    private int scheduleId;
    private String content;
    private String date;
    private int userId;
    private String nickname;
    private boolean editable;

    public static Comparator<ScheduleRes> getComparatorByDate() {
        return Comparator.comparing(ScheduleRes::getDate);
    }
}
