package com.example.demo.src.data.dto.diary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DiaryInfo {
    private int diaryId;
    private String content;
    private String date;
}
