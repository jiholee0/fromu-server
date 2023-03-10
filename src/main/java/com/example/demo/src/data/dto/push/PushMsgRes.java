package com.example.demo.src.data.dto.push;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PushMsgRes {
    private int userId;
    private String title;
    private String body;
    private boolean success;
}
