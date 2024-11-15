package com.board.service;

import com.board.domain.Board;
import com.board.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    
    private final BoardMapper boardMapper;
    
    public List<Board> getAllBoards() {
        return boardMapper.findAll();
    }
    
    public Board getBoard(Long id) {
        boardMapper.updateViewCount(id);
        return boardMapper.findById(id);
    }
    
    @Transactional
    public void saveBoard(Board board) {
        boardMapper.insert(board);
    }
    
    @Transactional
    public void updateBoard(Board board) {
        boardMapper.update(board);
    }
    
    @Transactional
    public void deleteBoard(Long id) {
        boardMapper.delete(id);
    }
}