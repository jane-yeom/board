package com.board.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Email {
    private Long id;
    private String sender;
    private String receiver;
    private String subject;
    private String content;
    private LocalDateTime sentAt;
    private String status;
}
