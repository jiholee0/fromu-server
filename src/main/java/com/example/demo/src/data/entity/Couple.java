package com.example.demo.src.data.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "couple")
@SQLDelete(sql = "UPDATE couple SET delete_flag = true, user_id1 = 0, user_id2 = 0 WHERE couple_id = ?")
@NoArgsConstructor
public class Couple {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int coupleId;
    @Column(nullable = false)
    public int userId1;
    @Column(nullable = false)
    public int userId2;
    @Column(length = 6)
    public String mailboxName;
    @Column(length = 8)
    public String firstMetDay;
    @Column(length=40)
    public String pushMessage;

    public boolean deleteFlag;

    @Builder
    public Couple(int coupleId, int userId1, int userId2, String mailboxName, String firstMetDay, boolean deleteFlag) {
        this.coupleId = coupleId;
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.mailboxName = mailboxName;
        this.firstMetDay = firstMetDay;
        this.deleteFlag = deleteFlag;
        this.pushMessage = "빨리 일기장 내놔ㅡㅅㅡ";
    }

    public void modifyFirstMetDay(String str) { this.firstMetDay = str;}
    public void modifyMailbox(String str) {this.mailboxName = str;}
    public void modifyPushMessage(String str){this.pushMessage = str;}
}
