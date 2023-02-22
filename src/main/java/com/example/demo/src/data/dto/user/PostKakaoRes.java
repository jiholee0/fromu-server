package com.example.demo.src.data.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class PostKakaoRes {
    private Boolean isMember;
    private String email;
    private Integer userId;
    private String jwt;
}
