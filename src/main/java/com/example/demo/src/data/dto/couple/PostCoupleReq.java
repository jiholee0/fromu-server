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
    private int user2Id;

    @Builder
    public PostCoupleReq(int user1Id, int user2Id){
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }

    public Couple toEntity() {
        return Couple.builder()
                .user1Id(user1Id)
                .user2Id(user2Id)
                .deleteFlag(false)
                .build();
    }
}
