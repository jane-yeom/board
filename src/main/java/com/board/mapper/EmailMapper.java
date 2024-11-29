package com.board.mapper;

import com.board.domain.Email;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmailMapper {

    void save(Email email);
    List<Email> findAll();
    Email findById(Long id);


}
