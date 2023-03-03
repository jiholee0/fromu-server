package com.example.demo.src.data.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostSocialLoginRes {
    private boolean isMember;
    private UserInfo userInfo;
}
