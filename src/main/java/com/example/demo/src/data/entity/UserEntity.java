package com.example.demo.src.data.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "USER_TB")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column(length = 30, nullable = false)
    private String email;
    @Column(length = 6, nullable = false)
    private String nickname;
    @Column(length = 8, nullable = false)
    private String birthday;
    @Column(length = 2, nullable = false)
    private String gender;
    @Column(length = 8, nullable = false)
    private String firstMetDay;
    @Column(length = 10, nullable = false)
    private String userCode;
    private boolean deleteFlag;
    private String createDate;
    private String updateDate;

    @Builder
    public UserEntity(int userId, String email, String nickname, String birthday, String gender, String firstMetDay, String userCode, boolean deleteFlag, String createDate, String updateDate) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
        this.firstMetDay = firstMetDay;
        this.userCode = userCode;
        this.deleteFlag = deleteFlag;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}
