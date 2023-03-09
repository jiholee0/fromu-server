package com.example.demo.src.data.dto.letter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class GetLetterRes {
    private int letterId;
    private String mailboxName;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date time;
    private boolean readFlag;
}
