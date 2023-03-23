package com.example.demo.src.data.dto.diary;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DiaryResWithUserId {
    private int diaryId;
    private String writerNickname;
    private int writerUserId;
    private String content;
    @ApiModelProperty(allowableValues = "null")
    private String imageUrl;
    private String weather;
    private String date;
    private int day;
}
