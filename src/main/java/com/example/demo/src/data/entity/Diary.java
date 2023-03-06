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
    @Column
    public int weather;
    public boolean deleteFlag;

    @Builder
    public Diary(int diaryId, int diarybookId, int userId, String content, String imageUrl, int weather, boolean deleteFlag){
        this.diaryId = diaryId;
        this.diarybookId = diarybookId;
        this.userId = userId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.weather = weather;
        this.deleteFlag = deleteFlag;
    }

    public void modifyDiary(String content, String imageUrl, int weather){
        this.content = content;
        this.imageUrl = imageUrl;
        this.weather = weather;
    }
}
