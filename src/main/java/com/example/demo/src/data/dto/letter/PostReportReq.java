package com.example.demo.src.data.dto.letter;

import com.example.demo.src.data.entity.Report;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReportReq {
    private String content;

    @Builder
    public PostReportReq(String content){
        this.content = content;
    }

    public Report toEntity(int reporterUserId, int letterId){
        return Report.builder()
                .content(content)
                .reporterUserId(reporterUserId)
                .letterId(letterId)
                .build();
    }
}
