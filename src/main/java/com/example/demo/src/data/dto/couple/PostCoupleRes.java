package com.example.demo.src.data.dto.couple;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@AllArgsConstructor
public class PostCoupleRes {
    private int coupleId;
    private String partnerNickname;
}
