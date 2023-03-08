package com.example.demo.src.data.dto.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class PatchDeviceTokenRes {
    private int userId;
    private String deviceToken;
}
