package com.board.mapper;

import com.board.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void save(User user);

    User findByUsername(String username);

    User findByEmail(String email);

    void enable(Long id);
}
