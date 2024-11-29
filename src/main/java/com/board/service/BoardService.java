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

    private EmailService emailService;
    
    public List<Board> findAll(int page, int size) {

        int offset = (page -1) * size;
        return boardMapper.findAll(offset, size);
    }

    public int getTotalPages(int size){
        int totalCount = boardMapper.count();
        return (totalCount + size -1) / size;

    }
    
    public Board getBoard(Long id) {
        boardMapper.updateViewCount(id);
        return boardMapper.findById(id);
    }
    
    @Transactional
    public Long saveBoard(Board board) {
        boardMapper.insert(board);
        return board.getId();
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