package com.example.demo.src.data.dto.letter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostLetterRes {
    private int letterId;
    private String sendMailboxName;
    private String receiveMailboxName;
}
