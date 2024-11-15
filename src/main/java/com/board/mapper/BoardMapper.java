package com.board.mapper;

import com.board.domain.Board;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BoardMapper {
    List<Board> findAll();
    Board findById(Long id);
    void insert(Board board);
    void update(Board board);
    void delete(Long id);
    void updateViewCount(Long id);
}