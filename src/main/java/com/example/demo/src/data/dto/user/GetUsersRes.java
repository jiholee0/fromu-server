package com.example.demo.src.data.dto.user;
import com.example.demo.src.data.entity.User;
import com.example.demo.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.ParseException;

@Getter
@AllArgsConstructor
public class GetUsersRes {
    private final int userId;
    private String email;
    private String nickname;
    private String birthday;
    private String gender;
    private String firstMetDay;
    private String userCode;
    private boolean deleteFlag;

    public GetUsersRes(User entity) throws ParseException {
        this.userId = entity.getUserId();
        this.email = entity.getEmail();
        this.nickname = entity.getNickname();
        this.birthday = entity.getNickname();
        this.gender = entity.getGender();
        this.firstMetDay = entity.getFirstMetDay();
        this.userCode = entity.getUserCode();
        this.deleteFlag = entity.isDeleteFlag();
    }
}
