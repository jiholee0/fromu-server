package com.example.demo.src.data.dto.letter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchScoreRes {
    private int coupleId;
    private int fromCount;
    private int getScoreCoupleId;
    private int getScoreFromCount;
}
