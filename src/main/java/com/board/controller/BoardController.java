package com.board.controller;

import com.board.domain.Board;
import com.board.domain.Comment;
import com.board.domain.FileAttachment;
import com.board.service.BoardService;
import com.board.service.CommentService;
import com.board.service.FileService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;
    private final CommentService commentService;

    @Value("${file.upload.path}")
    private String uploadPath;
    
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1" )int page,
                       @RequestParam(defaultValue = "10") int size,
                         Model model) {
          List<Board> boards = boardService.findAll(page, size);
        int totalPages = boardService.getTotalPages(size);

        // 페이지 번호 범위 계산
        List<Integer> pageNumbers = new ArrayList<>();

        // 10개 단위로 페이지 네비게이션을 표시
        int startPage = (int) Math.floor((page - 1) / 10) * 10 + 1; // 시작 페이지 (현재 페이지를 기준으로 10개 단위)
        int endPage = Math.min(startPage + 9, totalPages); // 끝 페이지 (10페이지 범위로 제한)

        // 10개 페이지를 추가
        for (int i = startPage; i <= endPage; i++) {
            pageNumbers.add(i);
        }

       // 모델에 데이터 추가
        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", page);
        model.addAttribute("pages", pageNumbers);
        model.addAttribute("prevPage", startPage - 1);  // 이전 페이지 시작
        model.addAttribute("nextPage", endPage + 1);  // 다음 페이지 시작
        model.addAttribute("hasPrev", startPage > 1);  // 이전 페이지가 있는지 확인
        model.addAttribute("hasNext", endPage < totalPages);  // 다음 페이지가 있는지 확인

        return "board/list";
    }
    
    @GetMapping("/write")
    public String write() {
        return "board/write";
    }
    
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        Board board = boardService.getBoard(id);
        List<Comment> comments = commentService.getComments(id);
        String username = (String)session.getAttribute("username");
        model.addAttribute("board", board);
        model.addAttribute("files",fileService.getFilesByBoardId(id));
        model.addAttribute("comments", comments);
        model.addAttribute("username", username);
        return "board/detail";
    }
    
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.getBoard(id));
        return "board/edit";
    }
    
    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, Board board) {
        board.setId(id);
        boardService.updateBoard(board);
        return "redirect:/board/" + id;
    }
    
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return "redirect:/board/list";
    }

    @PostMapping("/insert")
    public String insert(Board board, @RequestParam("files") List<MultipartFile> files ) {
        try {
            Long boardId = boardService.saveBoard(board);
            // 게시글 저장
            //boardService.saveBoard(board);
            // 파일 저장
            fileService.saveFiles(boardId, files);

            return "redirect:/board/list";
        } catch (Exception e) {

            return "redirect:/board/write";
        }
    }
    @GetMapping("/file/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        try {
            FileAttachment fileAttachment = fileService.getFile(fileId);
            Path filePath = Paths.get(uploadPath + "/" + fileAttachment.getSavedFilename());
            Resource resource = new UrlResource(filePath.toUri());

            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + URLEncoder.encode(fileAttachment.getOriginalFilename(), "UTF-8") + "\"")
                .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 파일 삭제
    @DeleteMapping("/file/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) {
        try {
            fileService.deleteFile(fileId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}