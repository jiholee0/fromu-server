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
    private String weather;

    @Builder
    public DiaryReq(String content, String weather){
        this.content = content;
        this.weather = weather;
    }

    public Diary toEntity(int diarybookId, int userId, String imageUrl, String date){
        return Diary.builder()
                .diarybookId(diarybookId)
                .content(content)
                .weather(weather)
                .userId(userId)
                .imageUrl(imageUrl)
                .date(date)
                .deleteFlag(false)
                .build();
    }
}
