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
    public int user1Id;
    public int user2Id;
    @Column(length = 20, nullable = false)
    public String mailbox;
    public boolean deleteFlag;

    @Builder
    public Couple(int coupleId, int user1Id, int user2Id, String mailbox, boolean deleteFlag) {
        this.coupleId = coupleId;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.mailbox = mailbox;
        this.deleteFlag = deleteFlag;
    }

    public void modifyMailbox(String str) { this.mailbox = str; }
}
