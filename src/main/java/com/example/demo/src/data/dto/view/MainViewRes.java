package com.example.demo.src.data.dto.view;

import com.example.demo.src.data.entity.Diarybook;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class MainViewRes {
    private String nickname;
    private String partnerNickname;
    private int dDay;
    private int diarybookStatus;
    @ApiModelProperty(allowableValues = "null")
    private DiarybookDto diarybook;
}
