package com.example.demo.src.data.dto.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class DiarybookDto {
    private int diarybookId;
    private int coverNum;
    private String name;
    private int turnUserId;
    private String turnTime;
    private String imageUrl;
    private boolean deleteFlag;

    public DiarybookDto(int diarybookId, int coverNum, String name, int turnUserId, String turnTime, String imageUrl, boolean deleteFlag) {
        this.diarybookId = diarybookId;
        this.coverNum = coverNum;
        this.name = name;
        this.turnUserId = turnUserId;
        this.turnTime = turnTime;
        this.imageUrl = imageUrl;
        this.deleteFlag = deleteFlag;
    }
}
