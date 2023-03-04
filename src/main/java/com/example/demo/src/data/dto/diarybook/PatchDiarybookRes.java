package com.example.demo.src.data.dto.diarybook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchDiarybookRes {
    private int userId;
    private int diarybookId;
}
