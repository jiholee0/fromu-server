package com.example.demo.src.data.dto.couple;

import com.example.demo.src.data.entity.Couple;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCoupleReq {
    private int user1Id;
    private String user2Code;

    @Builder
    public PostCoupleReq(int user1Id, String user2Code){
        this.user1Id = user1Id;
        this.user2Code = user2Code;
    }

    public Couple toEntity(int user2Id) {
        return Couple.builder()
                .user1Id(user1Id)
                .user2Id(user2Id)
                .deleteFlag(false)
                .build();
    }
}
