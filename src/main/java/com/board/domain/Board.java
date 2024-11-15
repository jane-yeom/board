package com.board.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Board {
    private Long id;
    private String title;
    private String content;
    private String author;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}