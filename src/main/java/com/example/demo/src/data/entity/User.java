package com.example.demo.src.data.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "user")
@SQLDelete(sql = "UPDATE user SET delete_flag = true WHERE user_id = ?")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int userId;
    @Column(length = 30, nullable = false)
    public String email;
    @Column(length = 6, nullable = false)
    public String nickname;
    @Column(length = 8, nullable = false)
    public String birthday;
    @Column(length = 2, nullable = false)
    public String gender;

    @Column(length = 10)
    public String userCode;
    public boolean deleteFlag;

    @Builder
    public User(int userId, String email, String nickname, String birthday, String gender, String userCode, boolean deleteFlag) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
        this.userCode = userCode;
        this.deleteFlag = deleteFlag;
    }

    public void modifyNickname(String str) { this.nickname = str; }
    public void modifyBirthday(String str) { this.birthday = str; }
}
