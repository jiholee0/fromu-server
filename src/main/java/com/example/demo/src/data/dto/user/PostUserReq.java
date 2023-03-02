package com.example.demo.src.data.dto.user;

import com.example.demo.src.data.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUserReq {
    @ApiModelProperty(example = "~@email.com")
    private String email;
    @ApiModelProperty(example = "벨라")
    private String nickname;
    @ApiModelProperty(example = "20010607")
    private String birthday;
    @ApiModelProperty(example = "FM")
    private String gender;

    @Builder
    public PostUserReq(String email, String nickname, String birthday, String gender) {
        this.email = email;
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
    }

    public User toEntity(String userCode){
        return User.builder()
                .email(email)
                .nickname(nickname)
                .birthday(birthday)
                .gender(gender)
                .userCode(userCode)
                .deleteFlag(false)
                .build();
    }
}
