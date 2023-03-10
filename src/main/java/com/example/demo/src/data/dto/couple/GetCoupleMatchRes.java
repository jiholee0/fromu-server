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

    public GetCoupleMatchRes(boolean isMatch, CoupleRes coupleRes){
        this.isMatch = isMatch;
        this.coupleRes = coupleRes;
    }
}
