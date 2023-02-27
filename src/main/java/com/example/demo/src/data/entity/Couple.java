package com.example.demo.src.data.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "couple")
@SQLDelete(sql = "UPDATE couple SET delete_flag = true WHERE couple_id = ?")
@NoArgsConstructor
public class Couple {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int coupleId;
    @Column(nullable = false)
    public int userId1;
    @Column(nullable = false)
    public int userId2;
    @Column
    public int mailboxId;
    @Column(length = 8)
    public String firstMetDay;
    public boolean deleteFlag;

    @Builder
    public Couple(int coupleId, int userId1, int userId2, int mailboxId, String firstMetDay, boolean deleteFlag) {
        this.coupleId = coupleId;
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.mailboxId = mailboxId;
        this.firstMetDay = firstMetDay;
        this.deleteFlag = deleteFlag;
    }

    public void modifyFirstMetDay(String str) { this.firstMetDay = str;}
}
