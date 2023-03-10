package com.example.demo.src.data.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostSocialLoginRes {
    private boolean isMember;
    @ApiModelProperty(allowableValues = "null")
    private UserInfo userInfo;
}
