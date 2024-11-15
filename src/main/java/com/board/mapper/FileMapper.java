package com.board.mapper;

import com.board.domain.FileAttachment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {
    void insert(FileAttachment fileAttachment);
    List<FileAttachment> findByBoardId(Long boardId);
    FileAttachment findById(Long id);
    void deleteByBoardId(Long boardId);
    void delete(Long id);
}
