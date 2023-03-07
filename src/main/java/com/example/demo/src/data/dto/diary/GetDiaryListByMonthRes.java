package com.example.demo.src.data.dto.diary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetDiaryListByMonthRes {
    private String month;
    private List<DiaryInfo> diaryInfoList;
}
