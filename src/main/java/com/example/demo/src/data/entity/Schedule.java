package com.example.demo.src.data.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@Table(name = "schedule")
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public int scheduleId;
    @Column
    public int coupleId;
    @Column
    public int userId;
    @Column(length = 20)
    public String content;
    @Column(length = 8)
    public String date;

    @Builder
    public Schedule(int scheduleId, int coupleId, int userId, String content, String date){
        this.scheduleId = scheduleId;
        this.content = content;
        this.coupleId = coupleId;
        this.userId = userId;
        this.date = date;
    }

    public void modifyContent(String content){
        this.content = content;
    }
}
