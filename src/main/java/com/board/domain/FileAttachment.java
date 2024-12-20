package com.board.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileAttachment {

    private Long id;
    private Long boardId;
    private String originalFilename;
    private String savedFilename;
    private Long fileSize;
    private LocalDateTime createdAt;
}
