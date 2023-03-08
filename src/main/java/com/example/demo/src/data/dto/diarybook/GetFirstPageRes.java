package com.example.demo.src.data.dto.diarybook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFirstPageRes {
    private int diarybookId;
    private String name;
    private String imageUrl;
}
