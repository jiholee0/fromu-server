package com.example.demo.src.data.dto.diarybook;

import com.example.demo.src.data.entity.Diarybook;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDiarybookReq {
    private int coverNum;
    private String name;

    @Builder
    public PostDiarybookReq(int coverNum, String name) {
        this.coverNum = coverNum;
        this.name = name;
    }

    public Diarybook toEntity(int userId, int coupleId){
        return Diarybook.builder()
                .coverNum(coverNum)
                .name(name)
                .build();
    }
}
