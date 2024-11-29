package com.board.controller;

import com.board.domain.Board;
import com.board.domain.Comment;
import com.board.service.BoardService;
import com.board.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/comment")
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/add")
    public String addComment(Comment comment, HttpSession session){
        String username = (String) session.getAttribute("username");
        comment.setWriter(username);

        commentService.addComment(comment);
        return "redirect:/board/"+comment.getBoardId();
    }

    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable Long id, @RequestParam Long boardId, HttpSession session){
        Comment comment = commentService.findById(id);
        String username = (String) session.getAttribute("username");
        if(comment.getWriter().equals(username)){
            commentService.deleteComment(id);
        }

        return "redirect:/board/"+ boardId;

    }

}
