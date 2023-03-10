package com.example.demo.src.data.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@Table(name = "letter")
@NoArgsConstructor
public class Letter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int letterId;
    @Column
    public int refLetterId;
    @Column
    public int writerUserId;
    @Column
    public int sendCoupleId;
    @Column
    public int receiveCoupleId;
    @Column
    public String content;
    @Column
    public int stampNum;
    @Column
    public boolean readFlag;
    @Column
    public boolean reportFlag;
    @Column
    public int score;
    @Column
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    public Date createDate;

    @Builder
    public Letter(int letterId, int refLetterId, int writerUserId, int sendCoupleId, int receiveCoupleId, String content, int stampNum, boolean readFlag, boolean reportFlag, int score, Date createDate){
        this.content = content;
        this.letterId = letterId;
        this.readFlag = readFlag;
        this.writerUserId=writerUserId;
        this.reportFlag = reportFlag;
        this.receiveCoupleId = receiveCoupleId;
        this.refLetterId = refLetterId;
        this.stampNum = stampNum;
        this.sendCoupleId = sendCoupleId;
        this.createDate = createDate;
        this.score = score;
    }

    public void report(){this.reportFlag = true;}
    public void read(){this.readFlag = true;}
    public void score(int score){this.score = score;}
}
