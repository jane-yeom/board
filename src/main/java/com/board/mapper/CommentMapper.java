package com.board.mapper;

import com.board.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    void save(Comment comment);
    List<Comment> findByBoardId(Long boardId);
    void delete(Long id);
    Comment findById(Long id);
}
