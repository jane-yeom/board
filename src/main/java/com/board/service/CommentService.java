package com.board.service;

import com.board.domain.Comment;
import com.board.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentMapper commentMapper;

    public void addComment(Comment comment){
        commentMapper.save(comment);
    }

    public List<Comment> getComments(Long boardId){
        return commentMapper.findByBoardId(boardId);
    }

    public void deleteComment(Long id){
        commentMapper.delete(id);

    }

    public Comment findById(Long id){
        return commentMapper.findById(id);

    }
}
