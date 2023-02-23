package com.example.demo.src.data.dto.user;

import com.example.demo.src.data.entity.User;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUserReq {
    private String email;
    private String nickname;
    private String birthday;
    private String gender;
    private String firstMetDay;

    @Builder
    public PostUserReq(String email, String nickname, String birthday, String gender, String firstMetDay) {
        this.email = email;
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
        this.firstMetDay = firstMetDay;
    }

    public User toEntity(String userCode){
        return User.builder()
                .email(email)
                .nickname(nickname)
                .birthday(birthday)
                .gender(gender)
                .firstMetDay(firstMetDay)
                .userCode(userCode)
                .deleteFlag(false)
                .build();
    }
}
