package com.example.demo.src.data.dto.couple;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CoupleRes {
    private int coupleId;
    private boolean isSetMailboxName;
    private String nickname;
    private String partnerNickname;
}
