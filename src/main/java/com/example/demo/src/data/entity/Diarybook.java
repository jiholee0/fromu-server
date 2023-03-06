package com.example.demo.src.data.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Entity
@Table(name="diarybook")
@SQLDelete(sql = "UPDATE diarybook SET delete_flag = true WHERE diarybook_id = ?")
@NoArgsConstructor
public class Diarybook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int diarybookId;
    @Column
    public int coupleId;
    @Column
    public int coverNum;
    @Column(length = 20)
    public String name;
    @Schema(description = "일기장을 작성할 차례인 유저")
    @Column
    public int turnUserId;
    @Schema(required = false, description = "일기장이 오는 시간(일기장 전송으로부터 한시간 후)")
    @Column
    public Date turnTime;
    @Column
    public String imageUrl;
    public boolean deleteFlag;

    @Builder
    public Diarybook(int diarybookId, int coupleId, int coverNum, String name, int turnUserId, Date turnTime, String imageUrl, boolean deleteFlag) {
        this.diarybookId = diarybookId;
        this.coupleId = coupleId;
        this.coverNum = coverNum;
        this.name = name;
        this.turnUserId = turnUserId;
        this.turnTime = turnTime;
        this.imageUrl = imageUrl;
        this.deleteFlag = deleteFlag;
    }

    public void modifyDiarybookName(String str) {this.name = str;}
    public void modifyDiarybookCover(int num) {this.coverNum = num;}
    public void uploadDiarybookImage(String imageUrl){this.imageUrl = imageUrl;}
    public void updateDiary(int userId, Date turnTime){
        this.turnUserId=userId;
        this.turnTime = turnTime;
    }
}
