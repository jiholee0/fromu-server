package com.example.demo.src.data.dto.couple;

import com.example.demo.src.data.entity.Couple;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCoupleReq {

    public Couple toEntity(int userId1, int userId2) {
        return Couple.builder()
                .userId1(userId1)
                .userId2(userId2)
                .deleteFlag(false)
                .build();
    }
}
