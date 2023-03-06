package com.example.demo.src.data.dto.diary;

import com.example.demo.src.data.entity.Diary;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiaryReq {
    private String content;
    private int weather;

    @Builder
    public DiaryReq(String content, int weather){
        this.content = content;
        this.weather = weather;
    }

    public Diary toEntity(int diarybookId, int userId, String imageUrl){
        return Diary.builder()
                .diarybookId(diarybookId)
                .content(content)
                .weather(weather)
                .userId(userId)
                .imageUrl(imageUrl)
                .deleteFlag(false)
                .build();
    }
}
