package com.example.demo.src.data.dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MailboxViewRes {
    private int coupleId;
    private String mailboxName;
    private int newLetterId;
}
