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
@Table(name = "notice")
@NoArgsConstructor
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int noticeId;
    @Column
    public int coupleId;
    @Column
    public String content;
    @Column
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    public Date createDate;

    @Builder
    public Notice(int coupleId, String content, Date date){
        this.coupleId = coupleId;
        this.content = content;
        this.createDate = date;
    }
}
