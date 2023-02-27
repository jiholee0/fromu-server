package com.example.demo.src.data.dto.couple;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetCoupleMatchRes {
    private boolean isMatch;
    private CoupleRes coupleRes;

    public GetCoupleMatchRes(boolean isMatch, CoupleRes coupleRes){
        this.isMatch = isMatch;
        this.coupleRes = coupleRes;
    }
}
