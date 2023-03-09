package com.example.demo.src.data.dto.letter;

import com.example.demo.src.data.entity.Letter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLetterReq {
    private String content;
    private int stamp;

    @Builder
    public PostLetterReq(String content, int stamp){
        this.content = content;
        this.stamp = stamp;
    }

    public Letter toEntity(int refLetterId, int writerUserId, int sendCoupleId, int receiveCoupleId){
        return Letter.builder()
                .content(content)
                .stamp(stamp)
                .refLetterId(refLetterId)
                .writerUserId(writerUserId)
                .sendCoupleId(sendCoupleId)
                .receiveCoupleId(receiveCoupleId)
                .score(-1)
                .createDate(new Date())
                .readFlag(false)
                .reportFlag(false)
                .build();
    }
}
