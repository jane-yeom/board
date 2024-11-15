package com.board.controller;

import com.board.domain.Board;
import com.board.domain.FileAttachment;
import com.board.service.BoardService;
import com.board.service.FileService;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;

    @Value("${file.upload.path}")
    private String uploadPath;
    
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("boards", boardService.getAllBoards());
        return "board/list";
    }
    
    @GetMapping("/write")
    public String write() {
        return "board/write";
    }
    
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.getBoard(id));
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
            // 게시글 저장
            boardService.saveBoard(board);

            // 파일 저장
            fileService.saveFiles(board.getId(), files);

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