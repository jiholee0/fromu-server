package com.example.demo.src.data.dto.diarybook;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFirstPageRes {
    private int diarybookId;
    private String name;
    @ApiModelProperty(allowableValues = "null")
    private String imageUrl;
    private boolean writeFlag;
}
