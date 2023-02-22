package com.example.demo.src.data.dto.user;

import com.example.demo.src.data.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUserReq {
    private String email;
    private String nickname;
    private String birthday;
    private String gender;
    private String firstMetDay;
}
