package com.example.demo.src.data.dto.couple;

import com.example.demo.src.data.entity.Couple;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class GetCoupleMatchRes {
    private boolean isMatch;
    private CoupleRes coupleRes;

    public GetCoupleMatchRes(boolean isMatch, CoupleRes coupleRes){
        this.isMatch = isMatch;
        this.coupleRes = coupleRes;
    }
}
