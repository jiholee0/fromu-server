package com.example.demo.src.data.dto.couple;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetCoupleMatchRes {
    private boolean isMatch;
    @ApiModelProperty(allowableValues = "null", notes = "nullable")
    private CoupleRes coupleRes;
    @ApiModelProperty(allowableValues = "dDay 설정이 아직 되지 않았으면 0")
    private int dDay;

    public GetCoupleMatchRes(boolean isMatch, CoupleRes coupleRes, int dDay){
        this.isMatch = isMatch;
        this.coupleRes = coupleRes;
        this.dDay = dDay;
    }
}
