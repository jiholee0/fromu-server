package com.example.demo.src.data.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

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
    @Column
    public int recentUserId;
    @Column
    public String imageUrl;
    public boolean deleteFlag;

    @Builder
    public Diarybook(int diarybookId, int coupleId, int coverNum, String name, int recentUserId, String imageUrl, boolean deleteFlag) {
        this.diarybookId = diarybookId;
        this.coupleId = coupleId;
        this.coverNum = coverNum;
        this.name = name;
        this.recentUserId = recentUserId;
        this.imageUrl = imageUrl;
        this.deleteFlag = deleteFlag;
    }

    public void modifyDiarybookName(String str) {this.name = str;}
    public void modifyDiarybookCover(int num) {this.coverNum = num;}
    public void uploadDiarybookImage(String imageUrl){this.imageUrl = imageUrl;}

}
