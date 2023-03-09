package com.example.demo.src.data.dto.diary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DiaryRes {
    private int diaryId;
    private String writerNickname;
    private String content;
    private String imageUrl;
    private String weather;
    private String date;
    private int day;
}
