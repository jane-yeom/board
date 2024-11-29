package com.board.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Comment {

    private Long id;
    private Long boardId;
    private String content;
    private String writer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
