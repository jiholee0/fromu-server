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
    public int user1Id;
    @Column(nullable = false)
    public int user2Id;
    public int mailbox_id;
    @Column(length = 8)
    public String firstMetDay;
    public boolean deleteFlag;

    @Builder
    public Couple(int coupleId, int user1Id, int user2Id, int mailbox_id, String firstMetDay, boolean deleteFlag) {
        this.coupleId = coupleId;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.mailbox_id = mailbox_id;
        this.firstMetDay = firstMetDay;
        this.deleteFlag = deleteFlag;
    }

    public void modifyFirstMetDay(String str) { this.firstMetDay = str;}
}
