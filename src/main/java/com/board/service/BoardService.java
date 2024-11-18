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

    public void createPost(Board board) {
        boardMapper.insert(board);

        // 게시글 작성 확인 메일 발송
        try {
            String emailContent = String.format(
                "<div style='margin:20px;'>" +
                "<h2>게시글이 성공적으로 등록되었습니다.</h2>" +
                "<p>제목: %s</p>" +
                "<p>작성자: %s</p>" +
                "<a href='http://localhost:8080/board/%d' style='padding:10px 20px; background:#007bff; color:#fff; text-decoration:none; border-radius:5px;'>" +
                "게시글 보기</a>" +
                "</div>",
                board.getTitle(),
                board.getAuthor(),
                board.getId()
            );

            emailService.sendEmail(board.getEmail(), "게시글 등록 알림", emailContent);
        } catch (Exception e) {
            // 이메일 발송 실패는 게시글 등록에 영향을 주지 않도록 처리
            e.printStackTrace();
        }
    }
}