package com.example.demo.src.data.dto.schedule;

import com.example.demo.src.data.entity.Schedule;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleReq {
    private String content;
    private String date;

    @Builder
    public ScheduleReq(String content, String date){
        this.content = content;
        this.date = date;
    }

    public Schedule toEntity(int userId, int coupleId){
        return Schedule.builder()
                .userId(userId)
                .coupleId(coupleId)
                .content(content)
                .date(date)
                .build();
    }
}
