package com.example.demo.src.data.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserRes {
    private int userId;
    private String userCode;
    private String jwt;
    private String refreshToken;
}
