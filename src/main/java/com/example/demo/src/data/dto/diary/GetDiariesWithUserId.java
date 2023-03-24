package com.example.demo.src.data.dto.diary;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetDiariesWithUserId {
    private int diaryId;
    private int writerUserId;
}
