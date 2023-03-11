package com.example.demo.src.data.dto.letter;

import com.example.demo.src.data.entity.Letter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FirstLetter {
    private String content;
    private int stampNum;

    @Builder
    public FirstLetter(String content, int stampNum){
        this.content = content;
        this.stampNum = stampNum;
    }

    public Letter toEntity(int refLetterId, int writerUserId, int sendCoupleId, int receiveCoupleId, Date date){
        return Letter.builder()
                .content(content)
                .stampNum(stampNum)
                .refLetterId(refLetterId)
                .writerUserId(writerUserId)
                .sendCoupleId(sendCoupleId)
                .receiveCoupleId(receiveCoupleId)
                .score(-1)
                .createDate(date)
                .readFlag(false)
                .reportFlag(false)
                .build();
    }
}
