package com.example.demo.src.data.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Getter
@Entity
@Table(name="diary")
@SQLDelete(sql = "UPDATE diary SET delete_flag = true WHERE diary_id = ?")
@NoArgsConstructor
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int diaryId;
    @Column
    public int diarybookId;
    @Column
    public int userId;
    @Column
    public String content;
    @Column
    public String imageUrl;
    @Column(length = 5)
    public String weather;
    public String date;
    public boolean deleteFlag;

    @Builder
    public Diary(int diaryId, int diarybookId, int userId, String content, String imageUrl, String weather, String date, boolean deleteFlag){
        this.diaryId = diaryId;
        this.diarybookId = diarybookId;
        this.userId = userId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.weather = weather;
        this.date = date;
        this.deleteFlag = deleteFlag;
    }

    public void modifyDiaryAll(String content, String imageUrl, String weather){
        this.content = content;
        this.imageUrl = imageUrl;
        this.weather = weather;
    }

    public void modifyDiaryExceptImage(String content, String weather){
        this.content = content;
        this.weather = weather;
    }
}
