package com.example.demo.src.data.dto.user;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRes {
    private int userId;
    private String email;
    private String nickname;
    private String birthday;
    private String gender;
    private String firstMetDay;
    private int dDay;
    private String userCode;
    private boolean deleteFlag;
}
