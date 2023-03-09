package com.example.demo.src.data.dto.letter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class PatchReadLetterRes {
    private int letterId;
    private int stamp;
    private String content;
    private String sendMailboxName;
    private String receiveMailboxName;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date time;
    private int status;
    private boolean replyFalg;
    private boolean scoreFlag;
}
