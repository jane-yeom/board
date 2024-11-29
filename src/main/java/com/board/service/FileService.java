package com.board.service;

import com.board.domain.FileAttachment;
import com.board.mapper.BoardMapper;
import com.board.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.upload.path}")
    private String uploadPath;

    private final FileMapper fileMapper;
    private final BoardMapper boardMapper;

    @Transactional
    public void saveFiles(Long boardId, List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) return;

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String originalFilename = file.getOriginalFilename();
            String savedFilename = UUID.randomUUID().toString() + "_" + originalFilename;

            File savedFile = new File(uploadPath, savedFilename);
            file.transferTo(savedFile);

            FileAttachment fileAttachment = FileAttachment.builder()
                    .boardId(boardId)
                    .originalFilename(originalFilename)
                    .savedFilename(savedFilename)
                    .fileSize(file.getSize())
                    .build();

            fileMapper.insert(fileAttachment);
        }
    }

    public List<FileAttachment> getFilesByBoardId(Long boardId){
        return fileMapper.findByBoardId(boardId);

    }

    public FileAttachment getFile(Long id) {
        return fileMapper.findById(id);
    }

    @Transactional
    public void deleteFile(Long id){
        FileAttachment file = fileMapper.findById(id);
        if(file != null){
            File savedFile = new File(uploadPath, file.getSavedFilename());
            if(savedFile.exists()){
                savedFile.delete();
            }
            fileMapper.delete(id);
        }

    }
    @Transactional
    public void deleteFilesByBoardId(Long boardId) {
        List<FileAttachment> files = fileMapper.findByBoardId(boardId);
        for (FileAttachment file : files) {
            File savedFile = new File(uploadPath, file.getSavedFilename());
            if (savedFile.exists()) {
                savedFile.delete();
            }
        }
        fileMapper.deleteByBoardId(boardId);
    }

}
