package com.example.demo.src.data.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfo {
    private boolean isMatch;
    private boolean isSetMailboxName;
    private Integer userId;
    private String jwt;
    private String email;
    private String userCode;
}
